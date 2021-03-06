package like.rpc.cn.client.core;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.LogFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import like.rpc.cn.client.RpcClientInitializer;
import like.rpc.cn.core.Constants;
import like.rpc.cn.core.exception.ExceptionFactory;
import like.rpc.cn.core.loadbalance.RandomLoadBalance;
import like.rpc.cn.core.util.EndPoint;
import like.rpc.cn.registry.Registry;
import like.rpc.cn.registry.RegistryEvent;
import like.rpc.cn.registry.RegistryEventCallBack;
import like.rpc.cn.registry.RegistryEventType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 连接管理器
 * <p>
 * 和服务端的连接管理
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:28
 */
@Slf4j
public class ConnectManager implements RegistryEventCallBack {

    private final Registry registry;
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private final Map<String, List<ChannelWrapper>> channelsByService = new LinkedHashMap<>();
    private final Set<String> serverName = new HashSet<>();

    public ConnectManager(final Registry registry) {
        this.registry = registry;
        this.registry.watch(this);
    }

    public Channel getChannel(final String serviceName) {
        int selected = this.fetchService(serviceName);

        final ChannelWrapper channelWrapper = channelsByService.get(serviceName).get(selected);

        return channelWrapper.getChannel();
    }

    @Override
    public void execute(final RegistryEvent event) {
        final String serviceName = event.getServiceName();
        final String host = event.getHost();
        final int port = event.getPort();
        if (event.getRegistryEventType() == RegistryEventType.DELETE) {
            Iterator<ChannelWrapper> iterator = channelsByService.get(serviceName).iterator();
            while (iterator.hasNext()) {
                EndPoint endpoint = iterator.next().getEndpoint();
                if (endpoint.getHost().equals(host) && (endpoint.getPort() == port)) {
                    iterator.remove();
                }
            }
        }
        if (event.getRegistryEventType() == RegistryEventType.PUT) {
            try {
                add(channelsByService.get(serviceName), doConnect(serviceName, host, port));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int fetchService(final String serviceName) {
        if (!channelsByService.containsKey(serviceName) || channelsByService.get(serviceName).size() < 1) {
            List<ChannelWrapper> channels = new LinkedList<>();

            registry.find(serviceName).forEach(endPoint -> {
                add(channels, this.connect(serviceName, endPoint));
            });

            if (channels.size() == 0)
                throw ExceptionFactory.rpc(StrFormatter.format("  {}  provider is empty , please check it ! ", serviceName));

            channelsByService.put(serviceName, channels);
        }
        return doSelect(channelsByService.get(serviceName).size());
    }

    private ChannelWrapper connect(final String serviceName, final EndPoint endPoint) {
        return doConnect(serviceName, endPoint.getHost(), endPoint.getPort());
    }

    private int doSelect(final int size) {
        // ServiceLoaderUtil.loadFirstAvailable(LoadBalance.class) todo 扩展
        // RpcConfigLoader.get(Constants.loadBalance)
        return new RandomLoadBalance().select(MapUtil.of(Constants.loadBalance, "123"), size);
    }

    private void add(final List<ChannelWrapper> channels, final ChannelWrapper connect) {
        if (ObjectUtil.isNull(connect)) return;
        channels.add(connect);
    }

    @SneakyThrows
    private ChannelWrapper doConnect(final String serviceName, final String host, final Integer port) {
        if (serverName.add(serviceName.concat(host).concat(port.toString()))) {

            LogFactory.get().info(" rpc add connect :{} {}:{}", serviceName, host, port);

            Bootstrap b = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcClientInitializer());
            Channel channel = b.connect(host, port).sync().channel();
            return new ChannelWrapper(new EndPoint(host, port), channel);
        } else return null;
    }

    private static class ChannelWrapper {


        private EndPoint endpoint;
        private Channel channel;

        public ChannelWrapper(EndPoint endpoint, Channel channel) {
            this.endpoint = endpoint;
            this.channel = channel;
        }

        public EndPoint getEndpoint() {
            return endpoint;
        }

        public Channel getChannel() {
            return channel;
        }

        @Override
        public String toString() {
            return endpoint.getHost() + ":" + endpoint.getPort();
        }
    }
}

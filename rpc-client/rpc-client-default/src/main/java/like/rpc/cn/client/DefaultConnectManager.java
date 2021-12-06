package like.rpc.cn.client;

import cn.hutool.core.util.ServiceLoaderUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import like.rpc.cn.core.Constants;
import like.rpc.cn.core.RpcConfigLoader;
import like.rpc.cn.core.loadbalance.LoadBalance;
import like.rpc.cn.core.util.EndPoint;
import like.rpc.cn.registry.Registry;
import like.rpc.cn.registry.RegistryEvent;
import like.rpc.cn.registry.RegistryEventCallBack;
import like.rpc.cn.registry.RegistryEventType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:28
 */
@Slf4j
public class DefaultConnectManager implements ConnectManager, RegistryEventCallBack {

    private final Registry registry;
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private final AtomicInteger roundRobin = new AtomicInteger(0);
    private final Map<String, List<ChannelWrapper>> channelsByService = new LinkedHashMap<>();

    public DefaultConnectManager(final Registry registry) {
        this.registry = registry;
        this.registry.watch(this).subscribe();
    }

    @Override
    public Mono<Channel> getChannel(final String serviceName) {
        return Mono.create(sink -> {
            int select = 0;
            try {
                int size = channelsByService.get(serviceName).size();

                if (!channelsByService.containsKey(serviceName)) {
                    List<ChannelWrapper> channels = new ArrayList<>();
                    registry.find(serviceName).subscribe(endPoint -> {
                        channels.add(this.connect(endPoint));
                    });
                    channelsByService.put(serviceName, channels);
                }
                select = ServiceLoaderUtil.loadFirstAvailable(LoadBalance.class)
                        .select(Map.of(Constants.loadBalance, RpcConfigLoader.get(Constants.loadBalance)), size);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final ChannelWrapper channelWrapper = channelsByService.get(serviceName).get(select);

            sink.success(channelWrapper.getChannel());
        });
    }

    private ChannelWrapper connect(final EndPoint endPoint) {
        return doConnect(endPoint.getHost(), endPoint.getPort());
    }

    @SneakyThrows
    private ChannelWrapper doConnect(final String host, final int port) {
        Bootstrap b = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
        Channel channel = b.connect(host, port).sync().channel();
        return new ChannelWrapper(new EndPoint(host, port), channel);
    }

    @Override
    public void execute(final RegistryEvent event) {
        if (event.getRegistryEventType() == RegistryEventType.DELETE) {

        }

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
package like.rpc.cn;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import like.rpc.cn.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class RpcServer {

    private String host = "127.0.0.1";
    private Registry registry;
    private int port = 2017;

    private Map<String, Object> handlerMap = new LinkedHashMap<>();

    public RpcServer(Registry registry) {
        this.registry = registry;
    }

    /**
     * 暴露接口
     * @param clazz   需要暴露的类
     * @param handler 暴露的类的实例
     * @return {@link RpcServer} this for fluent
     */
    public RpcServer exposeService(Class<?> clazz, Object handler) {
        handlerMap.put(clazz.getName(), handler);
        return this;
    }

    /**
     * 设置端口
     * @param port port
     * @return {@link RpcServer}  this for fluent
     */
    public RpcServer port(int port) {
        this.port = port;
        return this;
    }

    public void run() {
        Executors.newSingleThreadExecutor().submit(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInitializer(handlerMap))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = null;
            try {
                future = bootstrap.bind(port).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (String className : handlerMap.keySet()) {
                try {
                    registry.register(className, port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }
}

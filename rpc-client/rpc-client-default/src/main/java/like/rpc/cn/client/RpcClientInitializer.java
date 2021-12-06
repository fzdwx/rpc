package like.rpc.cn.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import like.rpc.cn.protocol.codec.RpcDecoder;
import like.rpc.cn.protocol.codec.RpcEncoder;
import like.rpc.cn.protocol.model.rpc.RpcRequest;
import like.rpc.cn.protocol.model.rpc.RpcResponse;

public class RpcClientInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new RpcEncoder(RpcRequest.class));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
        pipeline.addLast(new RpcDecoder(RpcResponse.class));
        pipeline.addLast(new RpcClientHandler());
    }
}
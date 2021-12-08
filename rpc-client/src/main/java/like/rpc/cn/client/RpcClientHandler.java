package like.rpc.cn.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import like.rpc.cn.client.context.RpcRequestHolder;
import like.rpc.cn.client.support.RpcFuture;
import like.rpc.cn.protocol.model.rpc.RpcResponse;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) {
        String requestId = response.getRequestId();
        RpcFuture future = RpcRequestHolder.get(requestId);
        if (null != future) {
            RpcRequestHolder.remove(requestId);
            future.done(response);
        }
    }
}

package like.rpc.cn;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import like.rpc.cn.protocol.model.rpc.RpcRequest;
import like.rpc.cn.protocol.model.rpc.RpcResponse;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * rpc服务器处理程序
 *
 * <pre>
 *     根据 {@link like.rpc.cn.protocol.model.rpc.RpcRequest}所携带的信息来调用{@link #handlerMap}中的对象，
 *     然后封装返回值-
 * </pre>
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021-12-08 21:29:40
 * @see SimpleChannelInboundHandler
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    // key: serviceClassName   value:  service instance
    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());

        // invoke method by reflection
        String className = rpcRequest.getClassName();
        Object handlerObj = handlerMap.get(className);

        Class<?> handlerClass = handlerObj.getClass();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();

        // JDK reflect
        Method method = handlerClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        Object result = null;
        try {
            result = method.invoke(handlerObj, parameters);
            response.setResult(result);
        } catch (Exception e) {
            response.setError(e.getCause());
        } finally {
            channelHandlerContext.writeAndFlush(response);
        }
    }
}

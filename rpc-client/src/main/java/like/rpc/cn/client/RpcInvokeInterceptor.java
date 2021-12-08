package like.rpc.cn.client;

import io.netty.channel.Channel;
import like.rpc.cn.client.context.RpcRequestHolder;
import like.rpc.cn.client.core.ConnectManager;
import like.rpc.cn.client.support.RpcFuture;
import like.rpc.cn.protocol.model.rpc.RpcRequest;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class RpcInvokeInterceptor {

    private ConnectManager connectManager;

    public RpcInvokeInterceptor(ConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] args, @Origin Method method) {
        String name = method.getDeclaringClass().getName();
        // create rpc request
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        AtomicReference<Object> result = new AtomicReference<>();

        // get a connect from connect manager
        final Channel channel = connectManager.getChannel(method.getDeclaringClass().getName());

        // send the rpc request via to connect

        RpcFuture future = new RpcFuture();
        RpcRequestHolder.put(request.getRequestId(), future);

        channel.writeAndFlush(request);

        try {
            result.set(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.get();
    }
}

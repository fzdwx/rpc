package like.rpc.cn.client;

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
    public Object intercept(@AllArguments Object[] args, @Origin Method method) throws Exception {
        String name = method.getDeclaringClass().getName();
        System.out.println(name);
        // create rpc request
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        // get a connect from connect manager
        AtomicReference<Object> result = new AtomicReference<>();
        connectManager.getChannel(method.getDeclaringClass().getName()).subscribe(channel -> {
            // send the rpc request via the connect

            RpcFuture future = new RpcFuture();
            RpcRequestHolder.put(request.getRequestId(), future);

            channel.writeAndFlush(request);

            try {
                result.set(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result.get();
    }
}

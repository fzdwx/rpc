package like.rpc.cn.client.core;

import like.rpc.cn.client.RpcInvokeInterceptor;
import like.rpc.cn.registry.Registry;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * rpc客户端实现
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:26
 */
public class RpcClient {

    private final Registry registry;
    private final Map<String, Object> proxyByClass = new LinkedHashMap<>();
    private final ConnectManager connectManager;

    public RpcClient(Registry registry) {
        this.registry = registry;
        this.connectManager = new ConnectManager(registry);
        //this.registry.watch();
    }

    @SneakyThrows
    public <T> T create(final Class<T> clazz) {
        if (!proxyByClass.containsKey(clazz.getName())) {

            T proxy = new ByteBuddy()
                    .subclass(clazz)
                    .method(ElementMatchers.isDeclaredBy(clazz)).intercept(MethodDelegation.to(new RpcInvokeInterceptor(connectManager)))
                    .make()
                    .load(getClass().getClassLoader())
                    .getLoaded()
                    .newInstance();

            proxyByClass.put(clazz.getName(), proxy);
        }
        return ( T ) proxyByClass.get(clazz.getName());
    }
}

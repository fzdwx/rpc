package like.rpc.cn.client;

import like.rpc.cn.registry.Registry;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

/**
 * 默认的rpc客户端实现
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:26
 */
public class DefaultRpcClient implements RpcClient {

    private final Registry registry;
    private final Map<String, Object> proxyByClass = new LinkedHashMap<>();
    private final DefaultConnectManager defaultConnectManager;

    public DefaultRpcClient(Registry registry) {
        this.registry = registry;
        this.defaultConnectManager = new DefaultConnectManager(registry);
        //this.registry.watch();
    }

    @SneakyThrows
    @Override
    public <T> Mono<T> create(final Class<T> clazz) {
        if (!proxyByClass.containsKey(clazz.getName())) {

            T proxy = new ByteBuddy()
                    .subclass(clazz)
                    .method(isDeclaredBy(clazz)).intercept(MethodDelegation.to(new RpcInvokeInterceptor(defaultConnectManager)))
                    .make()
                    .load(getClass().getClassLoader())
                    .getLoaded()
                    .newInstance();

            proxyByClass.put(clazz.getName(), proxy);
        }
        return Mono.just(( T ) proxyByClass.get(clazz.getName()));
    }
}

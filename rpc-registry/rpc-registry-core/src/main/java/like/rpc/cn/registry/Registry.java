package like.rpc.cn.registry;

import like.rpc.cn.core.util.EndPoint;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 注册服务的接口定义
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021-12-05 20:49:14
 */
public interface Registry {

    /**
     * 注册服务
     * @param serviceName 服务名称，通常是类名 比如说你添加{@link like.rpc.cn.core.annotation.RpcProvider}注解的类的实现的接口
     * @param port        服务的端口
     * @return {@link reactor.core.publisher.Mono}<{@link Void}>
     * @exception Exception 异常
     */
    Mono<Void> register(String serviceName, Integer port);

    /**
     * 取消注册
     * @param serviceName 服务名称，通常是类名 比如说你添加{@link like.rpc.cn.core.annotation.RpcProvider}注解的类的实现的接口
     * @return {@link reactor.core.publisher.Mono}<{@link Void}>
     */
    Mono<Void> cancelRegistered(String serviceName);

    /**
     * 查找了注册了 serviceName 的服务
     * @param serviceName 服务名称
     * @return {@link reactor.core.publisher.Flux}<{@link like.rpc.cn.core.util.EndPoint}>
     * @exception Exception 异常
     */
    Flux<EndPoint> find(String serviceName);

    Mono<Void> watch(RegistryEventCallBack callBack);
}

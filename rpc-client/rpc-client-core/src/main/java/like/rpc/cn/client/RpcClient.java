package like.rpc.cn.client;

import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:18
 */
public interface RpcClient {

    /**
     * 创建客户端
     *
     * @param clazz 服务提供者的类
     * @return {@link Mono}<{@link T}>
     */
    <T> Mono<T> create(Class<T> clazz);
}
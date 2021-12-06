package like.rpc.cn.client;

import io.netty.channel.Channel;
import reactor.core.publisher.Mono;

/**
 * 连接管理器
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:16
 */
public interface ConnectManager {

    /**
     * 获取通道
     *
     * @param serviceName 服务名称
     * @return {@link Mono}<{@link Channel}>
     */
    Mono<Channel> getChannel(String serviceName);
}
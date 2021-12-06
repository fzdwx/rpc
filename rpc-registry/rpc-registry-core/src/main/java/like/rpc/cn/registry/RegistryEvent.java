package like.rpc.cn.registry;

import like.rpc.cn.core.util.KV;
import lombok.Getter;

/**
 * 注册事件
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 21:10
 */
public class RegistryEvent {

    @Getter
    private String host;
    @Getter
    private String ip;

    @Getter
    private RegistryEventType registryEventType = RegistryEventType.UNRECOGNIZED;

    private RegistryEvent(final String host, final String  ip, final RegistryEventType registryEventType) {
        this.host = host;
        this.ip = ip;
        this.registryEventType = registryEventType;
    }

    public RegistryEvent() {
    }

    // ===================== 构建方法

    public static RegistryEvent of(final String host, final String  ip, RegistryEventType registryEventType) {
        return new RegistryEvent(host, ip, registryEventType);
    }

    public static RegistryEvent of() {
        return new RegistryEvent();
    }

    public RegistryEvent preKv(final String host) {
        this.host = host;
        return this;
    }

    public RegistryEvent kv( final String  ip) {
        this.ip = ip;
        return this;
    }

    public RegistryEvent evenType(RegistryEventType registryEventType) {
        this.registryEventType = registryEventType;
        return this;
    }
}
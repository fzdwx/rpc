package like.rpc.cn.registry;

import like.rpc.cn.core.util.KV;

/**
 * 注册事件
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 21:10
 */
public class RegistryEvent {

    private KV<String, String> preKv;
    private KV<String, String> kv;
    private RegistryEventType registryEventType = RegistryEventType.UNRECOGNIZED;

    private RegistryEvent(final KV<String, String> preKv, final KV<String, String> kv, final RegistryEventType registryEventType) {
        this.preKv = preKv;
        this.kv = kv;
        this.registryEventType = registryEventType;
    }

    public RegistryEvent() {
    }

    // ===================== 构建方法

    public static RegistryEvent of(KV<String, String> preKv, KV<String, String> kv, RegistryEventType registryEventType) {
        return new RegistryEvent(preKv, kv, registryEventType);
    }

    public static RegistryEvent of() {
        return new RegistryEvent();
    }

    public RegistryEvent preKv(KV<String, String> preKv) {
        this.preKv = preKv;
        return this;
    }

    public RegistryEvent kv(KV<String, String> kv) {
        this.kv = kv;
        return this;
    }

    public RegistryEvent evenType(RegistryEventType registryEventType) {
        this.registryEventType = registryEventType;
        return this;
    }
}

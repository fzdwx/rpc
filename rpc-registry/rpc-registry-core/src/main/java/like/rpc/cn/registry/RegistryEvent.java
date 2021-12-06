package like.rpc.cn.registry;

import lombok.Data;
import lombok.Getter;

/**
 * 注册事件
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 21:10
 */
@Data
public class RegistryEvent {

    private String serverName;
    private String host;
    private int port;

    @Getter
    private RegistryEventType registryEventType = RegistryEventType.UNRECOGNIZED;

    private RegistryEvent(final String host, final int port, final String serverName, final RegistryEventType registryEventType) {
        this.host = host;
        this.port = port;
        this.serverName = serverName;
        this.registryEventType = registryEventType;
    }

    private RegistryEvent() {
    }

    // ===================== 构建方法

    public static RegistryEvent of(final String host, final int port, final String serverName, RegistryEventType registryEventType) {
        return new RegistryEvent(host, port, serverName, registryEventType);
    }

    public static RegistryEvent of() {
        return new RegistryEvent();
    }

    public RegistryEvent serverName(final String serverName) {
        this.serverName = serverName;
        return this;
    }
    public RegistryEvent host(final String host) {
        this.host = host;
        return this;
    }

    public RegistryEvent port(final int port) {
        this.port = port;
        return this;
    }

    public RegistryEvent evenType(RegistryEventType registryEventType) {
        this.registryEventType = registryEventType;
        return this;
    }
}

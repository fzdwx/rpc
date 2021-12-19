package like.rpc.cn.springboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rpc.server")
@Data
public class ServerProperties {

    /** rpc server 对外暴露的端口 */
    private int port = 5544;
}

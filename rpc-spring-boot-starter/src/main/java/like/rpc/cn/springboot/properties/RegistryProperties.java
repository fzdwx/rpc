package like.rpc.cn.springboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/19 18:49
 */
@ConfigurationProperties(prefix = "rpc.registry")
@Data
public class RegistryProperties {

    /** 注册中心的地址 */
    private String address = "http://127.0.0.1:8848";

    /** 如果注册中心是用的nacos */
    private Nacos nacos = Nacos.defaultNacos();

    @Data
    public static class Nacos {

        private String dataId = "rpc.yaml";

        private String group = "public";

        public static Nacos defaultNacos() {
            return new Nacos();
        }
    }
}

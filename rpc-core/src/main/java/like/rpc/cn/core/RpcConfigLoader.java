package like.rpc.cn.core;

import cn.hutool.core.lang.Dict;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.yaml.YamlUtil;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RPC 配置加载
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 20:16
 */
public class RpcConfigLoader {

    private static final Log log = LogFactory.get();

    /** 配置信息 */
    private static Dict CONFIG;

    /** 是否加载过配置信息了 */
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    /**
     * 读取解析配置后得到的信息
     * @param key        配置的关键字
     * @param defaultVal 默认值
     * @return {@link T}
     */
    public static <T> T get(String key, T defaultVal) {
        if (!INITIALIZED.get()) {
            try {
                loadConfig();
                INITIALIZED.set(true);
            } catch (Exception e) {
                log.error("加载rpc config 失败 :{}", e.getMessage());
            }
        }
        return CONFIG.get(key, defaultVal);
    }

    /**
     * @see #get(String, Object)
     */
    public static <T> T get(String key) {
        return get(key, null);
    }

    /**
     * 加载配置信息
     */
    private static void loadConfig() {
        // load from classpath:/rpc.yaml
        final InputStream resourceAsStream = RpcConfigLoader.class.getClassLoader().getResourceAsStream("rpc.yaml");
        CONFIG = YamlUtil.load(resourceAsStream, Dict.class);

        // load from application.yaml

        // other
    }
}

package like.rpc.cn.client;

import like.rpc.cn.client.ConnectManager;
import like.rpc.cn.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 默认的rpc客户端实现
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:26
 */
public class DefaultRpcClient {

    private final Registry registry;
    private final Map<String, Object> proxyByClass = new LinkedHashMap<>();
    private final DefaultConnectManager defaultConnectManager;

    public DefaultRpcClient(Registry registry){
        this.registry = registry;
        this.defaultConnectManager = new DefaultConnectManager(registry);
        //this.registry.watch();
    }
}
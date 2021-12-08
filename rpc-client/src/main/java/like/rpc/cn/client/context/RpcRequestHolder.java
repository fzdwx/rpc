package like.rpc.cn.client.context;

import like.rpc.cn.client.support.RpcFuture;

import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc请求持有者
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/06 20:05:17
 */
public class RpcRequestHolder {

    private static ConcurrentHashMap<String, RpcFuture> processingRpc = new ConcurrentHashMap<>();

    public static void put(String requestId, RpcFuture rpcFuture) {
        processingRpc.put(requestId, rpcFuture);
    }

    public static RpcFuture get(String requestId) {
        return processingRpc.get(requestId);
    }

    public static void remove(String requestId) {
        processingRpc.remove(requestId);
    }
}

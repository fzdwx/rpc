package like.rpc.cn.protocol.model.rpc;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * rpc请求
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/06 18:32:28
 */
@Data
@Accessors(chain = true)
public class RpcRequest {

    /**
     * 请求id
     */
    private String requestId;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数
     */
    private Object[] parameters;
}
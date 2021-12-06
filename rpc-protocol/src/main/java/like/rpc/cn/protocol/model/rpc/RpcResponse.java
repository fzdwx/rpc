package like.rpc.cn.protocol.model.rpc;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * rpc响应
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/06 18:32:45
 */
@Data
@Accessors(chain = true)
public class RpcResponse {

    /**
     * 请求id
     */
    private String requestId;
    /**
     * 错误
     */
    private String error;
    /**
     * 结果
     */
    private Object result;
}
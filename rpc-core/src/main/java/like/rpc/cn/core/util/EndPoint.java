package like.rpc.cn.core.util;

import cn.hutool.core.text.StrPool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 20:13
 */
@AllArgsConstructor
@Getter
@Setter
public class EndPoint {

    /** ip 地址 */
    private final String host;

    /** 端口 */
    private final int port;

    @Override
    public String toString() {
        return host + StrPool.COLON + port;
    }
}

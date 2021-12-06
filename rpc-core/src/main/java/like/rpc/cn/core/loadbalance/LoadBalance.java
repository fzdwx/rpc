package like.rpc.cn.core.loadbalance;

import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:46
 */
public interface LoadBalance {

    /**
     * 选择
     *
     * @param config 配置
     * @param amount 量
     * @return int
     * @throws Exception 异常
     */
    int select(Map<String, String> config, int amount) throws Exception;
}
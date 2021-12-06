package like.rpc.cn.core.loadbalance;

import like.rpc.cn.core.exception.ExceptionFactory;

import java.util.Map;
import java.util.Random;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 19:47
 */
public class RandomLoadBalance implements LoadBalance {

    private final Random random = new Random();

    @Override
    public int select(final Map<String, String> config, final int amount) throws Exception {
        if (amount <= 0) {
            throw ExceptionFactory.rpc("RandomLoadBalance: no available items to select");
        } else if (amount == 1) {
            return 0;
        } else {
            return random.nextInt(amount);
        }
    }
}
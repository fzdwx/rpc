package like.rpc.cn.client.support;


import like.rpc.cn.protocol.model.rpc.RpcResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * rpc的异步future
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021-12-08 20:28:20
 * @see Future
 */
public class RpcFuture implements Future<Object> {

    private CountDownLatch latch = new CountDownLatch(1);

    private RpcResponse response;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException {
        return get(5, TimeUnit.SECONDS);
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException {
        boolean b = latch.await(timeout, unit);
        return response.getResult();
    }

    public void done(RpcResponse response) {
        this.response = response;
        latch.countDown();
    }
}

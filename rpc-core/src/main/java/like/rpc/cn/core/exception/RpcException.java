package like.rpc.cn.core.exception;

/**
 * rpc 异常
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 20:31
 */
public class RpcException extends RuntimeException {

    public RpcException(final String s) {
        super(s);
    }

    public RpcException(final Throwable error) {
        super(error);
    }
}

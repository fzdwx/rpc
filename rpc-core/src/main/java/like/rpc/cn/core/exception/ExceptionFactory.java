package like.rpc.cn.core.exception;

/**
 * 异常工厂
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 20:52
 */
public class ExceptionFactory {

    public static RpcException rpc(final String s) {
        return new RpcException(s);
    }

    public static RpcException rpc(final Throwable error) {
        return new RpcException(error);
    }
}

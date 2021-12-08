package like.rpc.cn.core;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/7 20:56
 */
public class HelloServiceImpl {

    public String hello(String name) {
        final int i = 10 / 0;
        return "Hello " + name;
    }
}

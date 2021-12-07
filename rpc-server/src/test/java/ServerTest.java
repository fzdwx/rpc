import like.rpc.cn.RpcServer;
import like.rpc.cn.core.HelloService;
import like.rpc.cn.core.HelloServiceImpl;
import like.rpc.cn.registry.NacosRegistry;
import lombok.SneakyThrows;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/7 20:44
 */
public class ServerTest {

    @SneakyThrows
    public static void main(String[] args) {
        final NacosRegistry registry = new NacosRegistry("localhost:8848", "test.yml", "public");
        final RpcServer rpcServer = new RpcServer(registry)
                .port(2222)
                .exposeService(HelloService.class, new HelloServiceImpl());

        rpcServer.run();
    }
}

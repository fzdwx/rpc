import like.rpc.cn.client.core.RpcClient;
import like.rpc.cn.core.HelloService;
import like.rpc.cn.registry.NacosRegistry;
import lombok.SneakyThrows;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/8 20:40
 */
public class ClientTest {

    @SneakyThrows
    public static void main(String[] args) {
        final NacosRegistry registry = new NacosRegistry("localhost:8848", "test.yml", "public");

        final RpcClient rpcClient = new RpcClient(registry);

        TimeUnit.SECONDS.sleep(3);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            final HelloService service = rpcClient.create(HelloService.class);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String s = service.hello("leo");
            System.out.println("====" + s);

            String s2 = service.hello("tom");
            System.out.println("====" + s2);

            String s3 = service.hello("jerry");
            System.out.println("====" + s3);

            System.out.println("==== rpc invoke finished...");

        }, 0, 2, TimeUnit.SECONDS);
    }
}

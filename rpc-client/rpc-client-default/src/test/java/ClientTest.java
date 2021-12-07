import like.rpc.cn.client.DefaultRpcClient;
import like.rpc.cn.core.HelloService;
import like.rpc.cn.registry.NacosRegistry;
import lombok.SneakyThrows;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/7 20:45
 */
public class ClientTest {

    @SneakyThrows
    public static void main(String[] args) {
        final NacosRegistry registry = new NacosRegistry("localhost:8848", "test.yml", "public");

        final DefaultRpcClient rpcClient = new DefaultRpcClient(registry);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    rpcClient.create(HelloService.class).subscribe(service -> {
                        String s = service.hello("leo");
                        System.out.println("====" + s);

                        String s2 = service.hello("tom");
                        System.out.println("====" + s2);

                        String s3 = service.hello("jerry");
                        System.out.println("====" + s3);

                        System.out.println("==== rpc invoke finished...");
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5, 3, TimeUnit.SECONDS);

        Thread.sleep(3000 * 1000);
    }
}

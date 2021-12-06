package like.rpc.cn.registry;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import like.rpc.cn.core.util.EndPoint;
import like.rpc.cn.core.util.IpUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 注册中心 nacos 实现
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 21:27
 */
public class NacosRegistry implements Registry {

    /**
     * 注册中心地址
     */
    private final String serverAddr;
    /**
     * 注册中心上存放配置文件的名字
     */
    private final String dataId;
    /**
     * 分组
     */
    private final String group;

    private final ConfigService configService;
    private final NamingService namingService;
    private final List<String> serviceNames;

    public NacosRegistry(final String serverAddr, final String dataId, final String group) throws NacosException {
        this.serverAddr = serverAddr;
        this.dataId = dataId;
        this.group = group;
        configService = NacosFactory.createConfigService(serverAddr);
        namingService = NacosFactory.createNamingService(serverAddr);
        serviceNames = new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            final NacosRegistry registry = new NacosRegistry("http://localhost:8848", "test.yaml", "public");
            registry.register("com.some.package.IHelloService", 8000).subscribe();
            registry.register("com.some.package.IMain", 8000).subscribe();
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(100000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mono<Void> register(final String serviceName, final Integer port) {
        return Mono.defer(() -> {
            try {
                namingService.registerInstance(serviceName, IpUtil.getCurrentIp(), port);
                serviceNames.add(serviceName);
            } catch (NacosException e) {
                e.printStackTrace();
            }
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> cancelRegistered(final String serviceName) {
        return null;
    }

    @Override
    public Flux<EndPoint> find(final String serviceName) {
        return null;
    }

    @Override
    public Mono<Void> watch(final RegistryEventCallBack callBack) {
        return Mono.defer(() -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                for (final String serviceName : serviceNames) {
                    try {
                        namingService.subscribe(serviceName, e -> {
                            final NamingEvent event = ( NamingEvent ) e;
                            for (Instance instance : event.getInstances()) {
                                final JSONObject instanceJson = JSONUtil.parseObj(instance);
                                final RegistryEvent registryEvent = RegistryEvent.of().host(instanceJson.getStr("ip")).port(instance.getPort()).serverName(instanceJson.getStr("serviceName").split("@@")[1]);
                                if (instance.isHealthy()) {
                                    callBack.execute(registryEvent.evenType(RegistryEventType.PUT));
                                } else {
                                    callBack.execute(registryEvent.evenType(RegistryEventType.DELETE));
                                }
                            }
                        });
                    } catch (NacosException e) {
                        e.printStackTrace();
                    }
                }
            });
            return Mono.empty();
        });
    }

    @Override
    public void shutDown() {
        try {
            if (!Objects.isNull(namingService)) {
                this.namingService.shutDown();
            }
            if (!Objects.isNull(configService)) {
                this.configService.shutDown();
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}

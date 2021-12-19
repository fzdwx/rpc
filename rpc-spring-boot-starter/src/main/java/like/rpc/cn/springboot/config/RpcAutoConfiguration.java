package like.rpc.cn.springboot.config;

import like.rpc.cn.RpcServer;
import like.rpc.cn.client.core.RpcClient;
import like.rpc.cn.registry.NacosRegistry;
import like.rpc.cn.registry.Registry;
import like.rpc.cn.spring.RpcInjectBeanPostProcessor;
import like.rpc.cn.spring.RpcServerRunner;
import like.rpc.cn.springboot.RpcProviderAnnotationBeanFactoryPostProcessor;
import like.rpc.cn.springboot.properties.RegistryProperties;
import like.rpc.cn.springboot.properties.ServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rpc spring boot 自动配置
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/19 18:48
 */
@Configuration
@EnableConfigurationProperties({RegistryProperties.class, ServerProperties.class})
public class RpcAutoConfiguration {

    @Bean
    public Registry registry(RegistryProperties properties) throws Exception {
        return new NacosRegistry(properties.getAddress(), properties.getNacos().getDataId(), properties.getNacos().getGroup());
    }

    @ConditionalOnProperty(prefix = "rpc.server", value = "enable", havingValue = "true")
    @Bean
    public RpcServer rpcServer(Registry registry, ServerProperties properties) {
        RpcServer server = new RpcServer(registry);
        server.port(properties.getPort());
        return server;
    }

    @ConditionalOnProperty(prefix = "rpc.server", value = "enable", havingValue = "true")
    @Bean
    public static RpcProviderAnnotationBeanFactoryPostProcessor beanFactoryPostProcessor(@Value("${rpc.annotation.package}") String packageName) {
        return new RpcProviderAnnotationBeanFactoryPostProcessor(packageName);
    }

    @ConditionalOnProperty(prefix = "rpc.server", value = "enable", havingValue = "true")
    @Bean
    public RpcServerRunner applicationListener() {
        return new RpcServerRunner();
    }

    @ConditionalOnProperty(prefix = "rpc.client", value = "enable", havingValue = "true")
    @Bean
    public RpcInjectBeanPostProcessor referenceAnnotationBeanPostProcessor() {
        return new RpcInjectBeanPostProcessor();
    }

    @ConditionalOnProperty(prefix = "rpc.client", value = "enable", havingValue = "true")
    @Bean
    public RpcClient client(Registry registry) {
        return new RpcClient(registry);
    }
}

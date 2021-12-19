package like.rpc.cn.springboot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * {@link like.rpc.cn.core.annotation.RpcProvider}bean工厂后处理器
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021-12-19 19:03:24
 * @see BeanFactoryPostProcessor
 * @see ApplicationContextAware
 */
public class RpcProviderAnnotationBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private String scanBasePackage;

    public RpcProviderAnnotationBeanFactoryPostProcessor(String scanBasePackage) {
        this.scanBasePackage = scanBasePackage;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        RpcProviderAnnotationScanner scanner = new RpcProviderAnnotationScanner(( BeanDefinitionRegistry ) beanFactory);
        scanner.setResourceLoader(this.applicationContext);
        scanner.scan(scanBasePackage);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

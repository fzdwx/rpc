package like.rpc.cn.spring;

import like.rpc.cn.RpcServer;
import like.rpc.cn.core.annotation.RpcProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * {@link like.rpc.cn.core.annotation.RpcProvider} 对外提供服务
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/19 18:38
 */
public class RpcProviderBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RpcProvider.class)) {
                RpcServer rpcServer = applicationContext.getBean(RpcServer.class);
                RpcProvider reference = field.getAnnotation(RpcProvider.class);
                try {
                    rpcServer.exposeService(reference.interfaceClazz(), bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

package like.rpc.cn.springboot;

import like.rpc.cn.core.annotation.RpcProvider;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * {@link like.rpc.cn.core.annotation.RpcProvider} 注释扫描仪
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021-12-19 19:02:58
 * @see ClassPathBeanDefinitionScanner
 */
public class RpcProviderAnnotationScanner extends ClassPathBeanDefinitionScanner {
    public RpcProviderAnnotationScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(RpcProvider.class));
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }
}

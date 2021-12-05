package like.rpc.cn.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供者,对外暴露服务.
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 20:05
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RpcProvider {
    
    /**
     * 填写实现的接口类
     * @return {@link Class}<{@link ?}>
     */
    Class<?> interfaceClazz() default void.class;
}

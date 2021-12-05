package like.rpc.cn.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务使用者,通过对字段添加 {@link like.rpc.cn.core.annotation.RpcInject}
 * 来引用远程服务
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 20:10
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcInject {
    
    /**
     * 代表要应用的接口类
     * @return {@link Class}<{@link ?}>
     */
    Class<?> interfaceClazz();
}

package like.rpc.cn.spring;

import like.rpc.cn.RpcServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * rpc server 启动程序
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/19 18:36
 */
public class RpcServerRunner implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        RpcServer server = applicationContext.getBean(RpcServer.class);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

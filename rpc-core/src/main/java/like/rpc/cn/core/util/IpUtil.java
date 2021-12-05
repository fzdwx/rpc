package like.rpc.cn.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 20:57
 */
public class IpUtil {

    public static String getCurrentIp(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

}

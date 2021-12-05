package like.rpc.cn.core.enums;

/**
 * 所有枚举的顶级父类
 *
 * <pre>
 *     C: code 状态码的类型
 *     M: message 消息的类型
 * </pre>
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021-12-05 20:32:41
 */
public interface EnumInfo<C, M> {

    /**
     * 状态码
     * @return {@link C}
     */
    C code();

    /**
     * 消息
     * @return {@link M}
     */
    M message();
}

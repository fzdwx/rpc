package like.rpc.cn.core.util;

import cn.hutool.core.text.StrPool;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * KV键值对工具类
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021/12/5 20:39
 */
@EqualsAndHashCode
public class KV<K, V> {

    @Getter
    private K key;
    @Getter
    private V value;

    private KV(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 提供简单的工厂方法创建
     */
    public static <K, V> KV<K, V> of(final K key, final V value) {
        return new KV<>(key, value);
    }

    /**
     * 提供简单的工厂方法创建
     */
    public static <K, V> KV<K, V> of() {
        return new KV<>(null, null);
    }

    /**
     * 设置Key的值
     */
    public KV<K, V> key(K key) {
        this.key = key;
        return this;
    }

    /**
     * 设置value的值
     */
    public KV<K, V> value(V value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return key + StrPool.COLON + value;
    }
}

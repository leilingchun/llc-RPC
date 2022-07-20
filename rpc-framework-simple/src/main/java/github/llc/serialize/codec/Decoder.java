package github.llc.serialize.codec;

/**
 * @author llc
 * @Description 反序列化
 * @Version
 * @date 2022/7/18 15:10
 */
public interface Decoder {
    /**
     *
     * @author llc
     * @description
     * @date 2022/7/18 15:11
     * @param bytes 二进制数组
     * @param clazz 待转换类型
     * @return T 泛型、返回的对象
     */

    <T> T decode(byte[] bytes, Class<T> clazz);
}

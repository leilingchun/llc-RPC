package github.llc.serialize.codec;

/**
 * @author llc
 * @Description 序列化
 * @Version
 * @date 2022/7/18 15:10
 */
public interface Encoder {
    byte[] encode(Object obj);
}

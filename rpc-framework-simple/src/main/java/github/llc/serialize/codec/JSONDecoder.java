package github.llc.serialize.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 15:10
 */
public class JSONDecoder implements Decoder{
    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}

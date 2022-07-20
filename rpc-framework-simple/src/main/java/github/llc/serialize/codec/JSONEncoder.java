package github.llc.serialize.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 15:16
 */
public class JSONEncoder implements Encoder{
    @Override
    /**
     *
     * @author llc
     * @description 讲Json对象转换为二进制数组
     * @date 2022/7/18 15:16
     * @param obj
     * @return byte[]
     */

    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }
}

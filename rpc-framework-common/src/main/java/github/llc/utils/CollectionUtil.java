package github.llc.utils;

import java.util.Collection;

/**
 * @author llc
 * @Description 集合工具类
 * @Version
 * @date 2022/7/20 10:01
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection<?> c){
        return c == null || c.isEmpty();
    }
}

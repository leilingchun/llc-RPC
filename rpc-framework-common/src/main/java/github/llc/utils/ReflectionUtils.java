package github.llc.utils;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author llc
 * @Description 反射工具类
 * @Version 0.1
 * @date 2022/7/18 14:48
 */
public class ReflectionUtils {

    /**
     *
     * @author llc
     * @description
     * @date 2022/7/18 14:50
     * @param clazz 待创建的对象类
     * @param <T> 对象类型 泛型
     * @return T 返回对象
     */

    public static <T> T newInstance(Class<T> clazz){
        try{
            return clazz.newInstance();
        } catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     * @author llc
     * @description 获取某个Class的公有方法
     * @date 2022/7/18 14:55
     * @param clazz
     * @return java.lang.reflect.Method[]
     */

    public static Method[] getPublicMethods(Class clazz){
        //返回当前类所有方法，不含父类
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> pmethods = new ArrayList<>();
        //过滤出public方法
        for(Method m : methods){
            if(Modifier.isPublic(m.getModifiers())){
                pmethods.add(m);
            }
        }
        return pmethods.toArray(new Method[0]);
    }

    public static Object invoke(Object obj, Method method, Object... args){
        try{
            /**
             * obj 是指调用哪个对象，当Method是静态方法时，obj传null
             * args 是动态参数
             */
            return method.invoke(obj,args);
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }
}

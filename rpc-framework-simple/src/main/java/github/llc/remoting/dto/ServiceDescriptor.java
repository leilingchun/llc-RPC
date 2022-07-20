package github.llc.remoting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDescriptor {
    private String clazz; //类名
    private String method; //方法名
    private String returnType; //返回类型
    private String[] parameterTypes; //参数类型

    public static ServiceDescriptor from(Class clazz, Method method){
        ServiceDescriptor sdp = new ServiceDescriptor();
        //设置参数
        sdp.setClazz(clazz.getName());
        sdp.setMethod(method.getName());
        sdp.setReturnType(method.getReturnType().getName());

        Class[] parameterClasses = method.getParameterTypes();
        String[] parameterTypes = new String[parameterClasses.length];
        for(int i = 0; i < parameterClasses.length; i++){
            parameterTypes[i] = parameterClasses[i].getName();
        }
        sdp.setParameterTypes(parameterTypes);
        return sdp;
    }

    //重写判断相等方法
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        //类型转换
        ServiceDescriptor that = (ServiceDescriptor) o;
        return this.toString().equals(o.toString());
    }
    //重写哈希值方法
    @Override
    public int hashCode(){
        return toString().hashCode();
    }
    @Override
    public String toString(){
        return  "clazz="+ clazz
                + ",method="+ method
                +",returnType="+ returnType
                +",parameterTypes="+ Arrays.toString(parameterTypes);
    }
}

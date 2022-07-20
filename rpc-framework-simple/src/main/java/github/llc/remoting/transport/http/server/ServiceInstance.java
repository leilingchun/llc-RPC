package github.llc.remoting.transport.http.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 21:00
 */
@Data
@AllArgsConstructor
public class ServiceInstance {
    private Object target;
    private Method method;
}

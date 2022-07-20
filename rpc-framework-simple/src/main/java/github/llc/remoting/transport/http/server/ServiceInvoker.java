package github.llc.remoting.transport.http.server;

import github.llc.remoting.dto.Request;
import github.llc.utils.ReflectionUtils;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 21:00
 */
public class ServiceInvoker {
    public Object invoke(ServiceInstance service, Request request){
        return ReflectionUtils.invoke(
                service.getTarget(),
                service.getMethod(),
                request.getParameters()
        );
    }
}

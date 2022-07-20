package github.llc.registry;

import github.llc.remoting.dto.Request;

import java.net.InetSocketAddress;

/**
 * @author llc
 * @Description 先测试使用原先的request格式
 * @Version
 * @date 2022/7/19 20:17
 */
public interface ServiceDiscovery {
    InetSocketAddress lookupService(Request request);
}

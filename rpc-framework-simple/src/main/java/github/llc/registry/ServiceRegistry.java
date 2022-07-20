package github.llc.registry;

import java.net.InetSocketAddress;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/19 20:17
 */
public interface ServiceRegistry {
    /**
     *
     * @author llc
     * @description 用于注册服务的接口
     * @date 2022/7/19 20:21
     * @param rpcServiceName
     * @param inetSocketAddress
     * @return void
     */

    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}

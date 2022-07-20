package github.llc.registry.zk;

import github.llc.registry.ServiceRegistry;
import github.llc.registry.zk.util.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/19 20:18
 */
@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {
    /**
     *
     * @author llc
     * @description 注册实现类
     * @date 2022/7/19 20:29
     * @param rpcServiceName 服务名
     * @param inetSocketAddress  服务地址
     * @return void
     */

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        //创建了节点
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        //获得客户端对象
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        //创建永久节点
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}

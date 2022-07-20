package github.llc.registry.zk;

import github.llc.enums.RpcErrorMessageEnum;
import github.llc.exception.RpcException;
import github.llc.loadbalance.LoadBalance;
import github.llc.loadbalance.loadbalancer.RandomLoadBalance;
import github.llc.registry.ServiceDiscovery;
import github.llc.registry.zk.util.CuratorUtils;
import github.llc.remoting.dto.Request;
import github.llc.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author llc
 * @Description  zookeeper服务发现
 * @Version
 * @date 2022/7/19 20:18
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl(){
        //暂时不用spi获得，采用手动吧。
        this.loadBalance = new RandomLoadBalance();
    }
    //原代码这里采用SPI的服务发现方式，直接获得了类
    //public ZkServiceDiscoveryImpl() {
    //    this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    //}

    @Override
    public InetSocketAddress lookupService(Request request) {
        //调用服务描述的toString方法,将其作为rpcServiceName
        //String rpcServiceName = request.getService().toString();
        //先进行修改
        String rpcServiceName = "github.llc.rpc.example.CalServicetest3version1";
        //先获得zookeeper客户端
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        //通过这个名字去找到所有的服务列表
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient,rpcServiceName);
        //找不到就报自定义异常
        if(CollectionUtil.isEmpty(serviceUrlList)){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND,rpcServiceName);
        }
        // load balancing 负载均衡
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList,request);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        //将地址分割 按":"  host:port形式
        String[] socketAddressArray = targetServiceUrl.split(":");
        // host
        String host = socketAddressArray[0];
        // port
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}

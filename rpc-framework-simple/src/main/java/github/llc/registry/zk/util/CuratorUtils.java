package github.llc.registry.zk.util;

import github.llc.enums.RpcConfigEnum;
import github.llc.utils.PropertiesFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author llc
 * @Description zookeeper client 客户端工具
 * @Version
 * @date 2022/7/19 20:19
 */
@Slf4j
public class CuratorUtils {
    //工具类设置zookeeper的基本量
    private static final int Base_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    //服务地址映射
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    //注册地址路径
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    //客户端框架
    private static CuratorFramework zkClient;
    //默认zookeeper服务器地址
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "192.168.71.130:2181";
    /**
     *
     * @author llc
     * @description  Create persistent nodes. Unlike temporary nodes, persistent nodes are not removed when the client disconnects
     * @date 2022/7/19 21:09
     * @param zkClient
     * @param path
     * @return void
     */

    public static void createPersistentNode(CuratorFramework zkClient, String path){
        try{
            //当前如果已有永久节点，就不创建；否则就创建
            if(REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null){
                log.info("The node already exists. The node is :[{}]",path);
            }else{
                //eg: /my-rpc/github.javaguide.HelloService/127.0.0.1:9999
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("The node was created successfully. The node is:[{}]", path);
            }
            REGISTERED_PATH_SET.add(path);
        }catch (Exception e){
            log.error("create persistent node for path [{}] fail", path);
        }
    }
    /**
     * 
     * @author llc
     * @description 获得所有子节点的信息
     * @date 2022/7/19 21:22
     * @param zkClient
     * @param rpcServiceName 
     * @return java.util.List<java.lang.String>
     */
    
    //获得子节点的信息 rpc service name eg:github.javaguide.HelloServicetest2version1
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName){

        //if(SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)){
        //    //如果服务列表已经存在了该服务，直接返回服务名
        //    return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        //}
        List<String> result = null;
        //服务路径
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try{
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName,result);
            registerWatcher(rpcServiceName, zkClient);
        }catch (Exception e){
            log.error("get children nodes for path [{}] fail", servicePath);
        }
        return result;
    }

    /**
     * 
     * @author llc
     * @description
     * @date 2022/7/19 21:29
     * @param zkClient
     * @param inetSocketAddress
     * @return void
     */
    
    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress){
        //删除注册信息
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try{
                if(p.endsWith(inetSocketAddress.toString())){
                    zkClient.delete().forPath(p);
                }
            }catch (Exception e){
                log.error("clear registry for path [{}] fail", p);
            }
        });
        log.info("All registered services on the server are cleared:[{}]", REGISTERED_PATH_SET.toString());
    }

    /**
     *
     * @author llc
     * @description 获得zookeeper客户端
     * @date 2022/7/19 21:31
     * @param
     * @return org.apache.curator.framework.CuratorFramework
     */

    public static CuratorFramework getZkClient(){

        //    RPC_CONFIG_PATH("rpc.properties"),
        //    ZK_ADDRESS("rpc.zookeeper.address");
        //
        //    private final String propertyValue;
        //    从文件名“rpc.properties”文件中读取zk的地址
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.ZK_ADDRESS.getPropertyValue());
        String zookeeperAddress = properties != null && properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue()) != null ? properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue()) : DEFAULT_ZOOKEEPER_ADDRESS;
        //如果该客户端已经启动，则直接返回客户端
        if(zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED){
            return zkClient;
        }

        // 重试策略 3次，并且每次重试之间会增加sleep time
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Base_SLEEP_TIME, MAX_RETRIES);
        //创建client 服务器对象
        zkClient = CuratorFrameworkFactory.builder()
                // 连接到服务器,也可以是一个服务集群
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        //启动服务
        zkClient.start();
        try{
            //等待30s 直到连接到zookeeper
            if(!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)){
                throw new RuntimeException("Time out waiting to connect to zk!");
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return zkClient;
    }

    /**
     * Registers to listen for changes to the specified node
     * 监听特点节点的变化
     * @param rpcServiceName rpc service name eg:github.javaguide.HelloServicetest2version
     */
    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) throws Exception {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

}

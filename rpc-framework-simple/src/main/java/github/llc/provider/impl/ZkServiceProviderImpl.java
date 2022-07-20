package github.llc.provider.impl;

import github.llc.config.RpcServiceConfig;
import github.llc.enums.RpcErrorMessageEnum;
import github.llc.exception.RpcException;
import github.llc.provider.ServiceProvider;
import github.llc.registry.ServiceRegistry;
import github.llc.registry.zk.ZkServiceDiscoveryImpl;
import github.llc.registry.zk.ZkServiceRegistryImpl;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/20 12:36
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {
    //提供服务的注册
    private final Map<String,Object> serviceMap;
    //已经注册的服务
    private final Set<String> registeredService;
    //调用zk注册接受的实现类来实现注册功能
    private final ServiceRegistry serviceRegistry;
    //无参构造器
    public ZkServiceProviderImpl(){
        //属性初始化
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        //提供注册服务的对象\
        // 在原代码中采用单例模式创建
        serviceRegistry = new ZkServiceRegistryImpl();
    }


    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        //这里可以直接固定名字
        //String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        String rpcServiceName = "FisrtServiceTest";
        if(registeredService.contains(rpcServiceName)){
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName,rpcServiceConfig.getService());
        //log.info("Add service: {} and interfaces:{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
        log.info("Add service:{}",rpcServiceName);
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if(null == service){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try{
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            //serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(),new InetSocketAddress(host, RpcServer.port));
            //其实就是注册服务
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(),new InetSocketAddress(host, 3000));
        } catch (UnknownHostException e){
            log.error("occur exception when getHostAddress", e);
        }
    }
}

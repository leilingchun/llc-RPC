package github.llc.rpcClient;

import github.llc.registry.ServiceDiscovery;
import github.llc.registry.zk.ZkServiceDiscoveryImpl;
import github.llc.serialize.codec.Decoder;
import github.llc.serialize.codec.Encoder;
import github.llc.utils.ReflectionUtils;

import java.lang.reflect.Proxy;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 22:00
 */
public class RpcClient {
    private RpcClientConfig config = new RpcClientConfig();
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;
    //public final ServiceDiscovery serviceDiscovery;

    public RpcClient(RpcClientConfig config){
        this.config = config;
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.selector = ReflectionUtils.newInstance(config.getSelectorClass());
        this.selector.init(
                this.config.getServers(),
                this.config.getConnectCount(),
                this.config.getTransportClass()
        );
        //服务发现
        //this.serviceDiscovery = new ZkServiceDiscoveryImpl();
    }

    public RpcClient() {
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.selector = ReflectionUtils.newInstance(config.getSelectorClass());
        this.selector.init(
                this.config.getServers(),
                this.config.getConnectCount(),
                this.config.getTransportClass()
        );
        //this.serviceDiscovery = new ZkServiceDiscoveryImpl();

    }
    /**
     * 获取动态代理类
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvoker(clazz,encoder,decoder,selector)
        );
    }
}

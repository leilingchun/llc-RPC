package github.llc.remoting.transport.http.server;

import github.llc.config.RpcServiceConfig;
import github.llc.provider.ServiceProvider;
import github.llc.provider.impl.ZkServiceProviderImpl;
import github.llc.remoting.dto.Request;
import github.llc.remoting.dto.Response;
import github.llc.remoting.transport.http.RequestHandler;
import github.llc.remoting.transport.http.TransportServer;
import github.llc.serialize.codec.Decoder;
import github.llc.serialize.codec.Encoder;
import github.llc.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 20:58
 */
@Slf4j
public class RpcServer {
    private RpcServerConfig config = new RpcServerConfig();  //server配置类
    private TransportServer net;                             //server网络模块
    private Encoder encoder;                                 //序列化类
    private Decoder decoder;                                 //反序列化类
    private ServiceManager serviceManager;                   //service管理类
    private ServiceInvoker serviceInvoker;                   //servive执行类
    //private final ServiceProvider serviceProvider;

    public RpcServer(RpcServerConfig config) {
        this.config = config;

        //net
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(), this.handler);

        //codec
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());

        //service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();
        //初始化
        //serviceProvider = new ZkServiceProviderImpl();

    }


    /**
     *
     * @author llc
     * @description
     * @date 2022/7/20 13:21
     * @param rpcServiceConfig  注册服务
     * @return void
     */

    //public void registerService(RpcServiceConfig rpcServiceConfig) {
    //    serviceProvider.publishService(rpcServiceConfig);
    //}

    /**
     * 注册函数，将一个类注册为rpc service，其中的所有public方法会被注册为rpc service
     * 这个函数会调用ServiceManager类的register方法
     *
     * @param interfaceClass 注册类的接口
     * @param bean           注册类的实现类
     * @param <T>            泛型
     */
    public <T> void register(Class<T> interfaceClass, T bean) {
        serviceManager.register(interfaceClass, bean);
    }

    /**
     * 开启网络模块
     */
    public void start() {
        this.net.start();
    }

    /**
     * 停止网络模块
     */
    public void stop() {
        this.net.stop();
    }


    public RpcServer() {
        //net
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(), this.handler);
        //codec
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());

        //service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();
        //serviceProvider = new ZkServiceProviderImpl();
    }

    /**
     * 处理网络请求的实现类
     * 在初始化网络模块的时候，作为参数传入
     * 先将input的二进制数据读取出来，然后反序列化成Request类
     * 再通过ServiceManager类的lookup函数找到该service
     * 然后通过ServiceInvoker类的invoke方法来执行服务
     * 然后将结果序列化，返回
     */
    private RequestHandler handler = new RequestHandler() {

        @Override
        public void onRequest(InputStream recive, OutputStream toResp) {
            Response resp = new Response();
            try {
                byte[] inBytes = IOUtils.readFully(recive, recive.available());
                Request request = decoder.decode(inBytes, Request.class);
                log.info("get request: {}", request);

                ServiceInstance sis = serviceManager.lookup(request);
                log.info("sis:"+sis.toString());
                Object ret = serviceInvoker.invoke(sis, request);

                resp.setData(ret);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                resp.setCode(1);
                resp.setMessage("RpcServer got error: " + e.getClass().getName()
                        + " : " + e.getMessage()
                );
            } finally {
                try {
                    //反序列
                    byte[] outBytes = encoder.encode(resp);
                    toResp.write(outBytes);
                    log.info("response client");
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    };
}

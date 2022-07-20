package github.llc.rpcClient;

import github.llc.remoting.dto.Request;
import github.llc.remoting.dto.Response;
import github.llc.remoting.dto.ServiceDescriptor;
import github.llc.remoting.transport.http.TransportClient;
import github.llc.serialize.codec.Decoder;
import github.llc.serialize.codec.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 21:46
 */
@Slf4j
//动态代理类
public class RemoteInvoker implements InvocationHandler {
    private Encoder encoder;
    private Class clazz;
    private Decoder decoder;
    private TransportSelector selector;

    //参数构造器
    RemoteInvoker(Class clazz,
                  Encoder encoder,
                  Decoder decoder,
                  TransportSelector selector){
        this.encoder = encoder;
        this.decoder = decoder;
        this.clazz = clazz;
        this.selector = selector;
    }

    /**
     *
     * @author llc
     * @description 代理类
     * @date 2022/7/18 21:48
     * @param proxy
     * @param method
     * @param args
     * @return java.lang.Object
     */

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz,method));
        request.setParameters(args);
        //调用服务
        Response response = invokeRemote(request);
        if(response == null || response.getCode() != 0){
            throw  new IllegalStateException("fail to invoke remote"+ response.getMessage());
        }
        return response.getData();
    }

    /**
     * 远程执行，被invoke调用
     * @param request client请求
     * @return 返回 Response类
     */
    private Response invokeRemote(Request request) {
        TransportClient client = null;
        Response resp = null;
        try{
            client = selector.select();

            byte[] outbytes = encoder.encode(request);
            InputStream revice= client.write(new ByteArrayInputStream(outbytes));

            byte[] inBytes = IOUtils.readFully(revice,revice.available());

            resp= decoder.decode(inBytes,Response.class);
        } catch (IOException e) {
            log.warn(e.getMessage(),e);
            resp = new Response();
            resp.setCode(1);
            resp.setMessage("RpcClient got error: "
                    + e.getClass()
                    +" : "+e.getMessage()
            );
        } finally {
            if(client!=null){
                selector.release(client);
            }
        }
        return  resp;
    }

}

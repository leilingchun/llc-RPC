package github.llc.rpc.example;

import github.llc.config.RpcServiceConfig;
import github.llc.remoting.transport.http.server.RpcServer;


/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 21:55
 */
public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        CalService calService = new CalServiceImpl();
        server.register(CalService.class,new CalServiceImpl());
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder().group("test3")
                .version("version1").service(calService).build();
        //server.registerService(rpcServiceConfig);
        server.start();
    }
}

package github.llc.rpc.example;

import github.llc.remoting.dto.Request;
import github.llc.rpcClient.RpcClient;

import java.net.InetSocketAddress;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 21:59
 */
public class Client {
    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        Request request = new Request();
        //InetSocketAddress inetSocketAddress = client.serviceDiscovery.lookupService(request);
        //System.out.println(inetSocketAddress.toString());
        CalService service = client.getProxy(CalService.class);

        int r1 = service.add(1,2);
        int r2 = service.minus(10,5);
        System.out.println(r1);
        System.out.println(r2);
    }
}

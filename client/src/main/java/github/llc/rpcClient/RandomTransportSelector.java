package github.llc.rpcClient;

import com.sun.deploy.util.ReflectionUtil;
import github.llc.remoting.dto.Peer;
import github.llc.remoting.transport.http.TransportClient;
import github.llc.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 21:40
 */
@Slf4j
public class RandomTransportSelector implements TransportSelector{
    //已经连接号的client
    private List<TransportClient> clients;

    public RandomTransportSelector(){
        //空参构造器
        clients = new ArrayList<>();
    }
    //线程不安全，加锁
    @Override
    public synchronized void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz) {
        count = Math.max(count,1);
        for(Peer peer : peers){
            for(int i = 0; i < count; i++){
                //新建服务对象
                TransportClient client = ReflectionUtils.newInstance(clazz);
                client.connect(peer);
                clients.add(client);
            }
            log.info("connect server: {}",peer);
        }
    }

    @Override
    public synchronized TransportClient select() {
        int i = new Random().nextInt(clients.size());
        return clients.remove(i);
    }

    @Override
    public void release(TransportClient client) {
        clients.add(client);
    }

    @Override
    public void close() {
        for(TransportClient client:clients){
            client.close();
        }
        clients.clear();
    }
}

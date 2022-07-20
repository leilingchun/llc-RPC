package github.llc.loadbalance.loadbalancer;

import github.llc.loadbalance.AbstractLoadBalance;
import github.llc.remoting.dto.Request;

import java.util.List;
import java.util.Random;

/**
 * @author llc
 * @Description 负载均衡抽象类的实现类  随机负载均衡算法
 * @Version
 * @date 2022/7/20 10:08
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> serviceUrlList, Request request) {
        Random random = new Random();
        //随机返回一个服务器地址
        return serviceUrlList.get(random.nextInt(serviceUrlList.size()));
    }
}

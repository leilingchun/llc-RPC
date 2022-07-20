package github.llc.loadbalance;

import github.llc.remoting.dto.Request;
import github.llc.utils.CollectionUtil;

import java.util.List;

/**
 * @author llc
 * @Description 继承抽象接口的抽象类，用于实现负载均衡算法
 * @Version
 * @date 2022/7/20 9:51
 */
public  abstract class AbstractLoadBalance implements LoadBalance{

    @Override
    public String selectServiceAddress(List<String> serviceUrlList, Request request) {
        //服务器的列表为空，则返回空值
        if(CollectionUtil.isEmpty(serviceUrlList)){
            return null;
        }
        //如果只有一个服务器，就直接返回服务器地址
        if(serviceUrlList.size() == 1){
            return serviceUrlList.get(0);
        }
        //返回负载均衡算法选择服务器地址
        return doSelect(serviceUrlList,request);

    }

    protected abstract String doSelect(List<String> serviceUrlList, Request request);
}

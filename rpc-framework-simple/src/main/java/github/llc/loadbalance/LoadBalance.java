package github.llc.loadbalance;

import github.llc.remoting.dto.Request;

import java.util.List;

/**
 * @author llc
 * @Description 定义负载均衡类的接口
 * @Version
 * @date 2022/7/20 9:51
 */
//抽象接口使用SPI注解
public interface LoadBalance {
    //这里采用的还是原先的Request,后期要改
/**
 *
 * @author llc
 * @description 选择服务地址
 * @date 2022/7/20 9:57
 * @param serviceUrlList
 * @param request
 * @return java.lang.String
 */


    String selectServiceAddress(List<String> serviceUrlList, Request request);
}

package github.llc.rpcClient;

import github.llc.remoting.dto.Peer;
import github.llc.remoting.transport.http.TransportClient;

import java.util.List;

/**
 * @author llc
 * @Description 表示选择哪个server去连接
 * @Version
 * @date 2022/7/18 21:35
 */
public interface TransportSelector {
    /**
     *
     * @author llc
     * @description
     * @date 2022/7/18 21:38
     * @param peers 可以连接的Server节点信息
     * @param count client与server之间的建立多少个连接
     * @param clazz client实现类
     * @return void
     */

    void init(List<Peer> peers, int count , Class<? extends TransportClient> clazz);
    /**
     *
     * @author llc
     * @description 选择一个transport与server做交互
     * @date 2022/7/18 21:38
     * @param
     * @return github.llc.remoting.transport.http.TransportClient
     */

    TransportClient select();
    /**
     *
     * @author llc
     * @description 释放用完的client
     * @date 2022/7/18 21:39
     * @param client
     * @return void
     */

    void release(TransportClient client);

    void close();
}

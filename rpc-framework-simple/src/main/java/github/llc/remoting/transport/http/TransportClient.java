package github.llc.remoting.transport.http;


import github.llc.remoting.dto.Peer;

import java.io.InputStream;

/**
 * 1. 创建连接
 * 2. 发送数据，并且等待响应
 * 3. 关闭连接
 */
public interface TransportClient {
    //连接
    void connect(Peer peer);

    //写数据
    InputStream write(InputStream data);

    //关闭连接
    void close();
}

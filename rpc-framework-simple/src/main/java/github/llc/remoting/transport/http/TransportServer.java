package github.llc.remoting.transport.http;

/**
 * 1. 启动、监听
 * 2. 接受请求
 * 3. 关闭监听
 */
public interface TransportServer {
    /**
     * 初始化，设置servlet
     * @param port 端口
     * @param handler 处理请求的方法
     */
    void init(int port,RequestHandler handler);
    //开始服务
    void start();
    //停止服务
    void stop();
}

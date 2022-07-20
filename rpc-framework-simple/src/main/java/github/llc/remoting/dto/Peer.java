package github.llc.remoting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author llc
 * @Description 表示网络传输的一点端点
 * @Version
 * @date 2022/7/18 15:20
 */
@Data // getter setter toString
@AllArgsConstructor //带所有字段的构造方法
public class Peer {
    //主机地址
    private String host;
    //端口号
    private int port;
}

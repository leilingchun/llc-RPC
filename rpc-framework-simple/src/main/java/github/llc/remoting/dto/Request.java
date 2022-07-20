package github.llc.remoting.dto;

import lombok.Data;

/**
 * @author llc
 * @Description 表示RPC请求的结构
 * @Version
 * @date 2022/7/18 15:24
 */
@Data
public class Request {
    private ServiceDescriptor service;
    private Object[] parameters;
}

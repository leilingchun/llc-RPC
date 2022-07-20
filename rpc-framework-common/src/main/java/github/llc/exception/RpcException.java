package github.llc.exception;

import github.llc.enums.RpcErrorMessageEnum;

/**
 * @author llc
 * @Description rpc异常类，定义异常情况
 * @Version
 * @date 2022/7/20 10:44
 */
public class RpcException extends RuntimeException{
    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail){
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }
    public RpcException(String message,Throwable cause){
        super(message,cause);
    }
    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum){
        super(rpcErrorMessageEnum.getMessage());
    }
}

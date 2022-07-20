package github.llc.remoting.dto;

import lombok.Data;

/**
 * @author llc
 * @Description 表示RPC响应
 * @Version
 * @date 2022/7/18 15:34
 */
/**
 * 
 * @author llc
 * @description
 * @date 2022/7/18 20:39
 * @param null 
 * @return 
 */

@Data
public class Response {
    private int code = 0;
    private String message = "ok";  // 错误信息
    private Object data; //返回的数据
}

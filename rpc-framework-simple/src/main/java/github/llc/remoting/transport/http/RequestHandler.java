package github.llc.remoting.transport.http;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 20:53
 */
public interface RequestHandler {
    void onRequest(InputStream recive, OutputStream toResp);
}

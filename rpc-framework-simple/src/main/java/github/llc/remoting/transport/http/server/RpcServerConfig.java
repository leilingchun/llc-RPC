package github.llc.remoting.transport.http.server;

import github.llc.remoting.transport.http.HTTPTransportServer;
import github.llc.remoting.transport.http.TransportServer;
import github.llc.serialize.codec.Decoder;
import github.llc.serialize.codec.Encoder;
import github.llc.serialize.codec.JSONDecoder;
import github.llc.serialize.codec.JSONEncoder;
import lombok.Data;

/**
 * @author llc
 * @Description server 配置
 * @Version
 * @date 2022/7/18 20:59
 */
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HTTPTransportServer.class;

    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;

    private int port = 3000;
}

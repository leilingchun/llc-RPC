package github.llc.rpcClient;

import github.llc.remoting.dto.Peer;
import github.llc.remoting.transport.http.HTTPTransportClient;
import github.llc.remoting.transport.http.TransportClient;
import github.llc.serialize.codec.Decoder;
import github.llc.serialize.codec.Encoder;
import github.llc.serialize.codec.JSONDecoder;
import github.llc.serialize.codec.JSONEncoder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 22:02
 */
@Data
public class RpcClientConfig {
    private Class<? extends TransportClient> transportClass = HTTPTransportClient.class;
    private Class <? extends Encoder> encoderClass = JSONEncoder.class;
    private Class <? extends Decoder> decoderClass = JSONDecoder.class;
    private Class<? extends TransportSelector> selectorClass = RandomTransportSelector.class;

    private int connectCount =1;
    private List<Peer> servers = Arrays.asList(new Peer("127.0.0.1",3000));
}

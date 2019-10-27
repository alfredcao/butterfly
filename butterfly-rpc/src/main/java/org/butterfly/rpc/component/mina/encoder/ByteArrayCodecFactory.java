package org.butterfly.rpc.component.mina.encoder;

/**
 * Created by HUAWEI on 2019/10/26.
 */

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 自定义解编码器工厂
 *
 */

public class ByteArrayCodecFactory implements ProtocolCodecFactory {

    private SimpleProtocolEncoder encoder;
    private SimpleCumulaBytesDecoder decoder;

    public ByteArrayCodecFactory() {
        encoder = new SimpleProtocolEncoder();
        decoder = new SimpleCumulaBytesDecoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

}

package com.leoyang.genghis.server.http;

/**
 * Created by yang.liu on 2018/10/25
 */
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class HttpServerProtocolCodecFactory extends
        DemuxingProtocolCodecFactory {
    public HttpServerProtocolCodecFactory() {
        super.addMessageDecoder(HttpRequestDecoder.class);
        super.addMessageEncoder(HttpResponseMessage.class,HttpResponseEncoder.class);
    }
//    private final DefaultDecoderAdapter decoder;
//    private final DefaultEncoderAdapter encoder;
//
//    public DefaultProtocolCodecFactory(final CommunicationConfig config) {
//        encoder = new DefaultEncoderAdapter();
//        decoder = new DefaultDecoderAdapter(config);
//    }
//
//    @Override
//    public ProtocolEncoder getEncoder(IoSession is) throws Exception {
//        return encoder;
//    }
//
//    @Override
//    public ProtocolDecoder getDecoder(IoSession is) throws Exception {
//        return decoder;
//    }

}

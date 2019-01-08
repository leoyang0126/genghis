package com.leoyang.genghis.server.http;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by yang.liu on 2018/10/25
 */
public class HttpResponseEncoder implements MessageEncoder {
//    @Override
//    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
//
//        IoBuffer buffer = IoBuffer.allocate(1024,false).setAutoExpand(true);
//        // buffer.putString("ffdddddd", Charset.forName("UTF-8").newEncoder());
//        //buffer.putObject(message);
//        // buffer.put((byte[]) message);
//        buffer.clear();
//        HttpResponseMessage respmessage = (HttpResponseMessage)message;
//        Map<String, String> map = respmessage.getHeaders();
//        ArrayListValuedHashMap listValuedHashMap = new ArrayListValuedHashMap();
//        for (Map.Entry<String,String> entry : map.entrySet()) {
//            listValuedHashMap.put(entry.getKey(),entry.getValue());
//        }
//        buffer.putObject(listValuedHashMap);
//        buffer.putString("ffdddddd", Charset.forName("UTF-8").newEncoder());
//        buffer.flip();
//        out.write(buffer);
//    }
    private static final Set<Class<?>> TYPES;

    static {
        Set<Class<?>> types = new HashSet<Class<?>>();
        types.add(HttpResponseMessage.class);
        TYPES = Collections.unmodifiableSet(types);
    }
    private static final byte[] CRLF = new byte[] { 0x0D, 0x0A };

    public HttpResponseEncoder() {
    }

    @Override
    public void encode(IoSession session, Object message,
                       ProtocolEncoderOutput out) throws Exception {
        HttpResponseMessage msg = (HttpResponseMessage) message;
        IoBuffer buf = IoBuffer.allocate(256);
        // Enable auto-expand for easier encoding
        buf.setAutoExpand(true);
        try {
            // output all headers except the content length
            CharsetEncoder encoder = Charset.defaultCharset().newEncoder();
            buf.putString("HTTP/1.1 ", encoder);
            buf.putString(String.valueOf(msg.getResponseCode()), encoder);
            switch (msg.getResponseCode()) {
                case HttpResponseMessage.HTTP_STATUS_SUCCESS:
                    buf.putString(" OK", encoder);
                    break;
                case HttpResponseMessage.HTTP_STATUS_NOT_FOUND:
                    buf.putString(" Not Found", encoder);
                    break;
            }
            buf.put(CRLF);
            for (Iterator it = msg.getHeaders().entrySet().iterator(); it
                    .hasNext();) {
                Entry entry = (Entry) it.next();
                buf.putString((String) entry.getKey(), encoder);
                buf.putString(": ", encoder);
                buf.putString((String) entry.getValue(), encoder);
                buf.put(CRLF);
            }
            // now the content length is the body length
            buf.putString("Content-Length: ", encoder);
            buf.putString(String.valueOf(msg.getBodyLength()), encoder);
            buf.put(CRLF);
            buf.put(CRLF);
            // add body
            buf.put(msg.getBody());
            //System.out.println("n+++++++");
            //for (int i=0; i<buf.position();i++)System.out.print(new String(new byte[]{buf.get(i)}));
            //System.out.println("n+++++++");
        } catch (CharacterCodingException ex) {
            ex.printStackTrace();
        }
        buf.flip();
        out.write(buf);
    }
    public Set<Class<?>> getMessageTypes() {
        return TYPES;
    }
}

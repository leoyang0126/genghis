package com.leoyang.genghis.server.tcp;
import java.net.InetSocketAddress;

import com.alibaba.fastjson.JSONObject;
import com.leoyang.genghis.server.util.StringUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.proxy.utils.ByteUtilities;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
/**
 * Created by yang.liu on 2018/10/26
 */
public class TcpMinaClient extends IoHandlerAdapter {
    private IoConnector connector;
    private static IoSession session;
    public TcpMinaClient() {
        connector = new NioSocketConnector();
        connector.setHandler(this);
        ConnectFuture connFuture = connector.connect(new InetSocketAddress("localhost", TcpMinaServer.PORT));
        connFuture.awaitUninterruptibly();
        session = connFuture.getSession();
        System.out.println("TCP 客户端启动");
    }
    public static void start() throws Exception {
        TcpMinaClient client = new TcpMinaClient();
        for(int j=0;j<2;j++){ // 发送两遍
//            byte[] bts = new byte[20];
//            for (int i = 0; i < 20; i++) {
//                bts[i] = (byte) i;
//            }
            // String msg = "hello";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("txCode",120001);
            jsonObject.put("cardNo","622262002020200220");
            sendMsg(jsonObject.toJSONString());
            Thread.sleep(2000);
        }
        // 关闭会话，待所有线程处理结束后
        client.connector.dispose(true);
    }

    public static void sendMsg(String msg) {
        IoBuffer buffer = IoBuffer.allocate(20);
        // 自动扩容
        buffer.setAutoExpand(true);
        // 自动收缩
        buffer.setAutoShrink(true);
        buffer.put(msg.getBytes());
        buffer.flip();
        session.write(buffer);
    }

    @Override
    public void messageReceived(IoSession iosession, Object message)
            throws Exception {
        IoBuffer bbuf = (IoBuffer) message;
        byte[] byten = new byte[bbuf.limit()];
        bbuf.get(byten, bbuf.position(), bbuf.limit());
        System.out.println("客户端收到消息--->" + StringUtil.hexStrToString(ByteUtilities.asHex(byten)));
    }
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        System.out.println("客户端异常");
        super.exceptionCaught(session, cause);
    }
    @Override
    public void messageSent(IoSession iosession, Object obj) throws Exception {
        System.out.println("客户端消息发送--->"+obj.toString());
        super.messageSent(iosession, obj);
    }
    @Override
    public void sessionClosed(IoSession iosession) throws Exception {
        System.out.println("客户端会话关闭");
        super.sessionClosed(iosession);
    }
    @Override
    public void sessionCreated(IoSession iosession) throws Exception {
        System.out.println("客户端会话创建");
        super.sessionCreated(iosession);
    }
    @Override
    public void sessionIdle(IoSession iosession, IdleStatus idlestatus)
            throws Exception {
        System.out.println("客户端会话休眠");
        super.sessionIdle(iosession, idlestatus);
    }
    @Override
    public void sessionOpened(IoSession iosession) throws Exception {
        System.out.println("客户端会话打开");
        super.sessionOpened(iosession);
    }
}

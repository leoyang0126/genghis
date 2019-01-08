package com.leoyang.genghis.server.http;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Objects;

/**
 * Created by yang.liu on 2018/10/25
 */
public class HttpServerHandle extends IoHandlerAdapter {

    private HttpHandler handler;

    public HttpHandler getHandler() {
        return handler;
    }

    public void setHandler(HttpHandler handler) {
        this.handler = handler;
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        HttpRequestMessage requestMessage = (HttpRequestMessage) message;
        HttpResponseMessage response = handler.handleMessage(requestMessage);
        if (Objects.nonNull(response)) {
            session.write(response);
            // session.write("hello world");
        }
    }

//    @Override
//    public void messageSent(IoSession session, Object message) throws Exception {
//        super.messageSent(session, message);
//    }
}

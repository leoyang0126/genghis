package com.leoyang.genghis.server.http;

import com.alibaba.fastjson.JSONObject;
import com.leoyang.genghis.server.annotations.RequestFactory;
import com.leoyang.genghis.server.annotations.RequestMapping;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;

/**
 * Created by yang.liu on 2018/10/25
 */
public class HttpServer {
    private static final int DEFAULT_PORT = 8080;
    private NioSocketAcceptor acceptor;
    private boolean isRunning;
    private String encoding;

    private HttpHandler httpHandler;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
        HttpRequestDecoder.defaultEncoding = encoding;
        // HttpResponseEncoder.defaultEncoding = encoding;
    }

    public static void start () {
        HttpServer server = new HttpServer();
        // server.setEncoding("GB2312");
        server.setEncoding("utf8");
        server.setHttpHandler(new HttpHandler() {
            @Override
            public HttpResponseMessage handleMessage(HttpRequestMessage request) {
                String level = request.getParameter("level");
                System.out.println(request.getParameter("level"));
                System.out.println(request.getContext());
                HttpResponseMessage response = new HttpResponseMessage();
                response.setContentType("text/plain");
                response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
                response.appendBody("CONNECTED\n");
                response.appendBody(level);
                JSONObject jsonObject = RequestFactory.strToJsonOjb(request.getHeaders());
                Object obj = RequestFactory.invoke("/"+request.getContext(),jsonObject);
                response.appendBody(obj.toString());
                return response;

            }
        });
        try {
            server.run(8880);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public void setHttpHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public void run(int port) throws Exception{

        synchronized (this) {
            if (isRunning) {
                System.out.println("Server is already running.");
                return;
            }
            acceptor = new NioSocketAcceptor();
            acceptor.getFilterChain().addLast(
                    "protocolFilter",
                    new ProtocolCodecFilter(
                            new HttpServerProtocolCodecFactory()));
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            HttpServerHandle handler = new HttpServerHandle();
            handler.setHandler(httpHandler);
            acceptor.setHandler(handler);
            acceptor.bind(new InetSocketAddress(port));
            isRunning = true;
            System.out.println("Server now listening on port " + port);
        }
    }

    /**
     * 停止监听HTTP服务
     */
    public void stop() {
        synchronized (this) {
            if (!isRunning) {
                System.out.println("Server is already stoped.");
                return;
            }
            isRunning = false;
            try {
                acceptor.unbind();
                acceptor.dispose();
                System.out.println("Server is stoped.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package com.leoyang.genghis.server.http;

/**
 * Created by yang.liu on 2018/10/25
 */
public interface HttpHandler {
    HttpResponseMessage handleMessage(HttpRequestMessage message);
}

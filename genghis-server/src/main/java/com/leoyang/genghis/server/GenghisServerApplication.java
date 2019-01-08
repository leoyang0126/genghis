package com.leoyang.genghis.server;

import com.leoyang.genghis.server.http.HttpServer;

/**
 * Created by yang.liu on 2018/11/9
 */
public class GenghisServerApplication {

    public static void main(String[] args) {
        HttpServer.start();
    }
}

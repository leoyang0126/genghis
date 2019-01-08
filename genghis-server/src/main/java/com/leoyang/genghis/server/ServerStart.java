package com.leoyang.genghis.server;

import com.leoyang.genghis.server.tcp.TcpMinaServer;

import java.io.IOException;

/**
 * Created by yang.liu on 2018/10/25
 */
public class ServerStart {
    public static void main(String[] args) {
        try {
            TcpMinaServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

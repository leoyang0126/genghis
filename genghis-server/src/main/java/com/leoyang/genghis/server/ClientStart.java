package com.leoyang.genghis.server;

import com.leoyang.genghis.server.tcp.TcpMinaClient;

/**
 * Created by yang.liu on 2018/10/26
 */
public class ClientStart {
    public static void main(String[] args) {
        try {
            TcpMinaClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

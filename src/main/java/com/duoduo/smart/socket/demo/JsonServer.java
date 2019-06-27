package com.duoduo.smart.socket.demo;

import org.smartboot.socket.transport.AioQuickServer;

import java.io.IOException;

/**
 * @author lixiaolong
 * @create 2019-06-26 14:06
 */
public class JsonServer {

    public static void main(String[] args) throws IOException {
        AioQuickServer<String> server = new AioQuickServer<String>(8888, new JsonProtocol(),new JsonServerProcessor());
        server.start();
    }

}

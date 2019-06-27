package com.duoduo.smart.socket.demo;

import org.smartboot.socket.transport.AioQuickClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;

/**
 * @author lixiaolong
 * @create 2019-06-26 14:07
 */
public class JsonClient {

    public static void main(String[] args) throws Exception {
        JsonClientProcessor processor = new JsonClientProcessor();
        AioQuickClient<String> aioQuickClient = new AioQuickClient<String>("localhost", 8888, new JsonProtocol(), processor);
        aioQuickClient.start();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            processor.getSession().write(String.format("你好，服务器，当前时间：%s$", LocalDateTime.now().format(formatter)));
        }
        Long end = System.currentTimeMillis();
        System.out.println(String.format("发送1亿条数据所花的时间(毫秒):%d",end - start));
        aioQuickClient.shutdown();
    }
}

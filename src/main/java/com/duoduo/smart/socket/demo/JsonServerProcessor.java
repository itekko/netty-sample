package com.duoduo.smart.socket.demo;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lixiaolong
 * @create 2019-06-26 14:00
 * @desc 消息处理
 */
public class JsonServerProcessor implements MessageProcessor<String> {

    @Override
    public void process(AioSession<String> session, String msg) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(String.format("服务器端接受到客户端的数据:%s",msg));
        try {
            session.write(String.format("服务器：客户端，你好,当前时间为:%s$", LocalDateTime.now().format(formatter)));
        } catch (IOException e) {


        }
    }

    @Override
    public void stateEvent(AioSession<String> session, StateMachineEnum stateMachineEnum, Throwable throwable) {

    }
}

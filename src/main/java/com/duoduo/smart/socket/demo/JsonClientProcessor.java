package com.duoduo.smart.socket.demo;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;

/**
 * @author lixiaolong
 * @create 2019-06-26 14:03
 */
public class JsonClientProcessor implements MessageProcessor<String> {
    private AioSession<String> session;

    @Override
    public void process(AioSession<String> session, String msg) {
        System.out.println(String.format("客户端端接受到的服务器端数据===>%s",msg));
    }

    @Override
    public void stateEvent(AioSession<String> session, StateMachineEnum stateMachineEnum, Throwable throwable) {
        switch (stateMachineEnum) {
            case NEW_SESSION:
                this.session = session;
                break;
            default:
                System.out.println("other state:" + stateMachineEnum);
        }
    }

    public AioSession<String> getSession() {
        return session;
    }
}

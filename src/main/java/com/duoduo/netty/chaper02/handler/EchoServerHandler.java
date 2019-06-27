package com.duoduo.netty.chaper02.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author lixiaolong
 * @create 2019-06-24 16:37
 * @desc 入栈处理类
 */
@ChannelHandler.Sharable // 标记handler能被多个channel共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 如果不捕获异常，会发生什么呢
     * 每个 Channel 都拥有一个与之相关联的 ChannelPipeline，其持有一个 ChannelHandler 的
     * 实例链。在默认的情况下， ChannelHandler 会把对它的方法的调用转发给链中的下一个 ChannelHandler。因此，如果 exceptionCaught()方法没有被该链中的某处实现，那么所接收的异常将会被
     * 传递到 ChannelPipeline 的尾端并被记录。为此，你的应用程序应该提供至少有一个实现了
     * exceptionCaught()方法的 ChannelHandler。
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace(); // 打印异常栈跟踪
        ctx.close(); // 关闭channel
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(String.format("EchoServerHandler msg %s",byteBuf.toString(CharsetUtil.UTF_8)));
        ctx.write(byteBuf); // 将接受到的消息写给发送者，而不冲刷出站消息


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//将未决消息冲刷到远程节点
                .addListener(ChannelFutureListener.CLOSE); //并且关闭该 Channel
    }
}

package com.duoduo.netty.chaper02.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author lixiaolong
 * @create 2019-06-24 17:25
 */
@ChannelHandler.Sharable // 标记handler能被多个channel共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 当被通知 Channel
         * 是活跃的时候，发
         * 送一条消息
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!$",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8)); //记录已接收消息的转储
    }
}

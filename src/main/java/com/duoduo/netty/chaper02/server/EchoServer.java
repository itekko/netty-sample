package com.duoduo.netty.chaper02.server;

import com.duoduo.netty.chaper02.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * SimpleChannelInboundHandler 与 ChannelInboundHandler
 * 你可能会想：为什么我们在客户端使用的是 SimpleChannelInboundHandler，而不是在 EchoServerHandler 中所使用的 ChannelInboundHandlerAdapter 呢？这和两个因素的相互作用有
 * 关：业务逻辑如何处理消息以及 Netty 如何管理资源。
 * 在客户端，当 channelRead0()方法完成时，你已经有了传入消息，并且已经处理完它了。当该方
 * 法返回时， SimpleChannelInboundHandler 负责释放指向保存该消息的 ByteBuf 的内存引用。
 * 在 EchoServerHandler 中，你仍然需要将传入消息回送给发送者，而 write()操作是异步的，直
 * 到 channelRead()方法返回后可能仍然没有完成（如代码清单 2-1 所示）。为此， EchoServerHandler
 * 扩展了 ChannelInboundHandlerAdapter，其在这个时间点上不会释放消息。
 * 消息在 EchoServerHandler 的 channelReadComplete()方法中，当 writeAndFlush()方
 * 法被调用时被释放（见代码清单 2-1）。
 * 第 5 章和第 6 章将对消息的资源管理进行详细的介绍。
 * @author lixiaolong
 * @create 2019-06-24 17:05
 */
public class EchoServer {

    // 端口
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(8000).start();
    }

    private void start() throws InterruptedException {

        final EchoServerHandler serverHandler = new EchoServerHandler();

        // 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class) //指定所使用的 NIO传输Channel
                    .localAddress(new InetSocketAddress(port)) //使用指定的端口设置套接字地址
                    .childHandler(new ChannelInitializer<SocketChannel>(){ // 添加一个 EchoServerHandler 到子 Channel的 ChannelPipeline
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            /**
                             * EchoServerHandler 被
                             * 标注为@Shareable，所
                             * 以我们可以总是使用
                             * 同样的实例
                             */
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            /**
             * 异步地绑定服务器；
             * 调用 sync()方法阻塞
             * 等待直到绑定完成
             */
            ChannelFuture f = b.bind().sync();

            /**
             * 获取 Channel 的
             * CloseFuture，并
             * 且 阻 塞 当 前 线程直到它完成
             */
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭 EventLoopGroup，并释放所有资源
            group.shutdownGracefully().sync();
        }

    }


}

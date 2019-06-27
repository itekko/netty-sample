package com.duoduo.netty.chaper03;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 常量值代表了由 class java.nio.channels.SelectionKey定义的位模式
 * OP_ACCEPT 请求在接受新连接并创建 Channel 时获得通知
 * OP_CONNECT 请求在建立一个连接时获得通知
 * OP_READ 请求当数据已经就绪，可以从 Channel 中读取时获得通知
 * OP_WRITE 请求当可以向 Channel 中写更多的数据时获得通知。这处理了套接字缓冲区被完
 * 全填满时的情况，这种情况通常发生在数据的发送速度比远程节点可处理的速度更
 * 快的时候
 *
 * @author lixiaolong
 * @create 2019-06-25 10:59
 */
public class PlainNioServer {
    public void serve(int port) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket ssocket = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ssocket.bind(address); // 绑定端口
        Selector selector = Selector.open(); // 打开多路复用器来处理channel
        serverChannel.register(selector, SelectionKey.OP_ACCEPT); // 将ServerSocket注册到Selector以接受连接
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        for (;;) {
            try {
                selector.select(); //等待需要处理的新事件；阻塞 将一直持续到下一个传入事件
            } catch (IOException ex) {
                ex.printStackTrace();
// handle exception
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys(); //获取所有接收事件的SelectionKey 实例
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server =
                                (ServerSocketChannel)key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE |
                                SelectionKey.OP_READ, msg.duplicate()); //接受客户端，并将它注册到选择器
                        System.out.println(
                                "Accepted connection from " + client);
                    }
                    if (key.isWritable()) { //检查事件是否是一个新的已经就绪可以被接受的连接
                        SocketChannel client =
                                (SocketChannel)key.channel();
                        ByteBuffer buffer =
                                (ByteBuffer)key.attachment();
                        while (buffer.hasRemaining()) {   // 将数据写到已连接的客户端
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close(); //关闭连接
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
// ignore on close
                    }
                }
            }
        }
    }
}

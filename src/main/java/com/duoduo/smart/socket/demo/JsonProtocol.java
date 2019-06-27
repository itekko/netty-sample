package com.duoduo.smart.socket.demo;

import io.netty.util.CharsetUtil;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.extension.decoder.DelimiterFrameDecoder;
import org.smartboot.socket.transport.AioSession;

import java.nio.ByteBuffer;

/**
 * @author lixiaolong
 * @create 2019-06-26 13:38
 * @desc 协议
 */
public class JsonProtocol implements Protocol<String> {

    private static final byte[] DELIMITER_BYTES = "$".getBytes(CharsetUtil.UTF_8);

    @Override
    public String decode(ByteBuffer buffer, AioSession<String> session, boolean eof) {
        DelimiterFrameDecoder delimiterFrameDecoder;
        if (session.getAttachment() == null) {//构造指定结束符的临时缓冲区
            delimiterFrameDecoder = new DelimiterFrameDecoder(DELIMITER_BYTES, 64);
            session.setAttachment(delimiterFrameDecoder);//缓存解码器已应对半包情况
        } else {
            delimiterFrameDecoder = (DelimiterFrameDecoder) session.getAttachment();
        }

        //未解析到DELIMITER_BYTES则返回null
        if (!delimiterFrameDecoder.decode(buffer)) {
            return null;
        }
        //解码成功
        ByteBuffer byteBuffer = delimiterFrameDecoder.getBuffer();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        session.setAttachment(null);//释放临时缓冲区
        return new String(bytes);
    }

    @Override
    public ByteBuffer encode(String msg, AioSession<String> session) {
        byte[] bytes = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length + DELIMITER_BYTES.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }
}

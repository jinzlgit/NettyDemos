package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Header-Content协议分包传输的字符串内容解码器
 *
 * @author Zhenlin Jin
 * @date 2021/7/15 9:55
 */
public class StringIntegerHeaderDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 可读字节小于4，消息头还没读满，返回
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        // 消息头已经完整
        // 在真正开始从缓冲区读取数据之前，调用markReaderIndex（）设置回滚点
        // 回滚点为消息头的readerIndex读指针位置
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();
        // 从缓冲区中读出消息头的大小，这会使得readIndex读指针前移
        // 剩余长度不够消息体，重置读指针
        if (byteBuf.readableBytes() < length) {
            // 读指针回滚到消息头的readIndex位置处，未进行状态的保存
            byteBuf.resetReaderIndex();
            return;
        }
        // 读取数据，编码成字符串
        byte[] inBytes = new byte[length];
        byteBuf.readBytes(inBytes, 0, length);
        list.add(new String(inBytes, "UTF-8"));
    }
}

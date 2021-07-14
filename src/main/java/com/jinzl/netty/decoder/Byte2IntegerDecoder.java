package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义整数解码器
 *
 * @author Zhenlin Jin
 * @date 2021/7/14 15:28
 */
public class Byte2IntegerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while (byteBuf.readableBytes() >= 4) {
            int i = byteBuf.readInt();
            System.out.println("解码出一个整数:" + i);
            list.add(i);
        }
    }
}

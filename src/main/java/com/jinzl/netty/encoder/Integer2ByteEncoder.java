package com.jinzl.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 整数编码为二进制的编码器
 *
 * @author Zhenlin Jin
 * @date 2021/7/15 14:06
 */
public class Integer2ByteEncoder extends MessageToByteEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer msg, ByteBuf out) throws Exception {
        out.writeInt(msg);
        System.out.println("encoder Integer:" + msg);
    }
}

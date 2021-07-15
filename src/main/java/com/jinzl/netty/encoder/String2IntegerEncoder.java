package com.jinzl.netty.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 将字符串中的所有数字提取出来
 *
 * @author Zhenlin Jin
 * @date 2021/7/15 14:26
 */
public class String2IntegerEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> out) throws Exception {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            // 48是0的编码，57是9的编码
            if (c >= 48 && c <= 57) {
                out.add(Integer.valueOf(String.valueOf(c)));
            }
        }
    }
}

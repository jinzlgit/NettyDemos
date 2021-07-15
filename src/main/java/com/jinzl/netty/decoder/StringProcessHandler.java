package com.jinzl.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 字符串分包处理器
 *
 * @author Zhenlin Jin
 * @date 2021/7/15 9:22
 */
public class StringProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String s = (String) msg;
        System.out.println("打印出一个字符串:" + s);
    }
}

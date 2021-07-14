package com.jinzl.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 解码后的业务处理器
 *
 * @author Zhenlin Jin
 * @date 2021/7/14 15:32
 */
public class IntegerProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Integer integer = (Integer) msg;
        System.out.println("打印出一个整数:" + integer);
    }
}

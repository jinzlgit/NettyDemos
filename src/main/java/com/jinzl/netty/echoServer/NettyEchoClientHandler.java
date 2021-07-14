package com.jinzl.netty.echoServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端处理器
 *
 * @author Zhenlin Jin
 * @date 2021/7/14 14:35
 */
@ChannelHandler.Sharable
public class NettyEchoClientHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoClientHandler INSTANCE
            = new NettyEchoClientHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int len = byteBuf.readableBytes();
        byte[] arr = new byte[len];
        byteBuf.getBytes(0, arr);
        System.out.println("客户端收到:" + new String(arr, "UTF-8"));

        // 释放ByteBuf的两种方法
        // 方法一：手动释放
        byteBuf.release();

        // 方法二：调用父类的入站方法，将msg向后传递
        // super.channelRead(ctx, msg);
    }
}

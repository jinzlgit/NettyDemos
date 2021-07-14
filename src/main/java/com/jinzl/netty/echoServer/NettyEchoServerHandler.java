package com.jinzl.netty.echoServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 处理器
 *
 * @author Zhenlin Jin
 * @date 2021/7/14 13:28
 */
@ChannelHandler.Sharable
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("msg type:" + (in.hasArray() ? "堆内存" : "直接内存"));
        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        System.out.println("server收到:" + new String(arr, "UTF-8"));

        System.out.println("写回前，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        // 写回客户端，异步任务
        ChannelFuture future = ctx.writeAndFlush(msg);
        future.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("写回后，msg.refCnt:" + ((ByteBuf) msg).refCnt());
            }
        });
    }
}

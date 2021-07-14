package com.jinzl.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 丢弃服务器
 *
 * @author Zhenlin Jin
 * @date 2021/7/13 10:59
 */
public class NettyDiscardServer {
    private final int serverPort;
    /**
     * 创建一个服务端的启动器
     */
    ServerBootstrap bootstrap = new ServerBootstrap();
    public NettyDiscardServer(int serverPort) {
        this.serverPort = serverPort;
    }
    public void runServer () {
        // 创建反应器线程组
        NioEventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            // 1.设置反应器线程组
            bootstrap.group(bossLoopGroup, workerLoopGroup);
            // 2.设置nio类型的通道
            bootstrap.channel(NioServerSocketChannel.class);
            // 3.设置监听端口
            bootstrap.localAddress(serverPort);
            // 4.设置通道的参数
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            // 5.装配子通道流水线
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                // 有连接到达时会创建一个通道
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    // 流水线管理子通道中的handler处理器
                    // 向子通道流水线添加一个handler处理器
                    socketChannel.pipeline().addLast(new NettyDiscardHandler());
                }
            });
            // 6.开始绑定服务器
            // 通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println("服务器启动成功，监听端口：" + channelFuture.channel().localAddress());
            // 7.等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8.关闭EventLoopGroup
            // 释放掉所有资源 包括创建的线程
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 6666;
        new NettyDiscardServer(port).runServer();
    }
}

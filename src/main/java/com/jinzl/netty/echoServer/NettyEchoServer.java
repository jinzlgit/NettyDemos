package com.jinzl.netty.echoServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty回显服务器
 *
 * @author Zhenlin Jin
 * @date 2021/7/14 13:08
 */
public class NettyEchoServer {
    private final int serverPort;

    /**
     * 启动器
     */
    private static final ServerBootstrap sb = new ServerBootstrap();

    public NettyEchoServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public void runServer() {
        // 创建反应器线程组
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 1.设置反应器线程组
            sb.group(boss, worker);
            // 2.设置nio类型的通道
            sb.channel(NioServerSocketChannel.class);
            // 3.设置监听端口
            sb.localAddress(serverPort);
            // 4.设置通道的参数
            sb.option(ChannelOption.SO_KEEPALIVE, true);
            sb.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            // 5.装配子通道流水线
            sb.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(NettyEchoServerHandler.INSTANCE);
                }
            });
            // 6.开始绑定服务器
            ChannelFuture channelFuture = sb.bind().sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("服务器启动成功");
                    }
                }
            });
            // 7.等待通道关闭的异步任务结束
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8.关闭EventLoopGroup
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 6666;
        new NettyEchoServer(port).runServer();
    }
}

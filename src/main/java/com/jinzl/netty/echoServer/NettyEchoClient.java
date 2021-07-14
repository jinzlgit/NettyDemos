package com.jinzl.netty.echoServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.tomcat.jni.Local;
import org.springframework.format.datetime.DateFormatter;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * 回显客户端
 *
 * @author Zhenlin Jin
 * @date 2021/7/14 13:52
 */
public class NettyEchoClient {
    private int serverPort;
    private String serverIp;

    private static final Bootstrap b = new Bootstrap();

    public NettyEchoClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void runClient() {
        // 创建反应器线程组
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 1.设置反应器线程组
            b.group(worker);
            // 2.设置nio类型的通道
            b.channel(NioSocketChannel.class);
            // 3.设置监听端口
            b.remoteAddress(serverIp, serverPort);
            // 4.设置通道参数
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            // 5.装配子通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                /**
                 * 有连接到达时会创建一个通道
                 */
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    // 流水线管理子通道中的handler业务处理器
                    // 向子通道流水线添加一个handler业务处理器
                    sc.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            ChannelFuture connect = b.connect();
            connect.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture cf) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("客户端连接成功");
                    } else {
                        System.out.println("客户端连接失败");
                    }
                }
            });
            // 阻塞，直到连接成功
            connect.sync();
            Channel channel = connect.channel();

            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入发送内容：");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while (scanner.hasNext()) {
                // 获取输入内容
                String next = scanner.nextLine();
                byte[] bytes = (formatter.format(LocalDateTime.now()) + " >>" + next).getBytes("UTF-8");
                // 发送 ByteBuf
                ByteBuf byteBuf = channel.alloc().buffer();
                byteBuf.writeBytes(bytes);
                channel.writeAndFlush(byteBuf);
                System.out.println("请输入发送内容：");
            }
        } catch (InterruptedException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            // 从容关闭 EventLoopGroup
            // 释放掉所有资源，包括创建的线程
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 6666;
        new NettyEchoClient(ip, port).runClient();
    }
}

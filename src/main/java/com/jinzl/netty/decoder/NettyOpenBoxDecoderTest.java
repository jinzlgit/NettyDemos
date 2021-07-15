package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Netty内置解码器使用示例：
 * <blockquote><pre>
 * 1.固定长度数据包解码器
 * 2.行分隔数据包解码器
 * 3.自定义分隔符数据包解码器
 * 4.自定义长度数据包解码器
 * </pre></blockquote>
 *
 * @author Zhenlin Jin
 * @date 2021/7/15 11:19
 */
public class NettyOpenBoxDecoderTest {

    /**
     * 行分割数据包解码器
     */
    @Test
    public void testLineBasedFrameDecoder() throws UnsupportedEncodingException {
        String split = "\r\n";
        String content = "Netty是个高性能的高并发框架";
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline()
                        .addLast(new LineBasedFrameDecoder(1024))
                        .addLast(new StringDecoder(Charset.forName("UTF-8")))
                        .addLast(new StringProcessHandler());
            }
        });

        for (int i = 0; i < 100; i++) {
            int random = (int) (Math.random() * 3 + 1);
            ByteBuf buffer = Unpooled.buffer();
            for (int j = 0; j < random; j++) {
                buffer.writeBytes(content.getBytes("UTF-8"));
            }
            buffer.writeBytes(split.getBytes("UTF-8"));
            channel.writeInbound(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义分隔符数据包解码器
     */
    @Test
    public void testDelimiterBasedFrameDecoder() throws UnsupportedEncodingException {
        String split = "\t";
        String content = "Netty是个高性能的高并发框架";

        final ByteBuf delimiter = Unpooled.copiedBuffer(split.getBytes("UTF-8"));

        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline()
                        .addLast(new DelimiterBasedFrameDecoder(1024, true, delimiter))
                        .addLast(new StringDecoder(Charset.forName("UTF-8")))
                        .addLast(new StringProcessHandler());
            }
        });

        for (int i = 0; i < 100; i++) {
            int random = (int) (Math.random() * 3 + 1);
            ByteBuf buffer = Unpooled.buffer();
            for (int j = 0; j < random; j++) {
                buffer.writeBytes(content.getBytes("UTF-8"));
            }
            buffer.writeBytes(split.getBytes("UTF-8"));
            channel.writeInbound(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义长度数据包解码器：Head-Content协议
     */
    @Test
    public void testLengthFieldBasedFrameDecoder() throws UnsupportedEncodingException {
        String content = "Netty是个高性能的高并发框架";

        LengthFieldBasedFrameDecoder decoder =
                new LengthFieldBasedFrameDecoder(1024, 0,
                        4, 0, 4);
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline()
                        .addLast(decoder)
                        .addLast(new StringDecoder(Charset.forName("UTF-8")))
                        .addLast(new StringProcessHandler());
            }
        });

        for (int i = 0; i < 100; i++) {
            ByteBuf buffer = Unpooled.buffer();
            String s = i + "次发送->" + content;
            byte[] bytes = s.getBytes("UTF-8");
            buffer.writeInt(bytes.length);
            buffer.writeBytes(bytes);
            channel.writeInbound(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义长度数据包解码器：多字段Head-Content协议
     */
    @Test
    public void testLengthFieldBasedFrameDecoder2() throws UnsupportedEncodingException {
        int VERSION = 100;
        String content = "Netty是个高性能的高并发框架";

        LengthFieldBasedFrameDecoder decoder =
                new LengthFieldBasedFrameDecoder(1024, 0,
                        4, 4, 8);

        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline()
                        .addLast(decoder)
                        .addLast(new StringDecoder(Charset.forName("UTF-8")))
                        .addLast(new StringProcessHandler());
            }
        });

        for (int i = 0; i < 100; i++) {
            ByteBuf buffer = Unpooled.buffer();
            String s = i + "次发送->" + content;
            byte[] bytes = s.getBytes("UTF-8");
            buffer.writeInt(bytes.length);
            buffer.writeInt(VERSION);
            buffer.writeBytes(bytes);
            channel.writeInbound(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

class IntegerProcessHandlerTest {
    @Test
    public void testByteToIntegerDecoder() {
        ChannelInitializer<EmbeddedChannel> initializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ec) throws Exception {
                ec.pipeline().addLast(new Byte2IntegerDecoder());
                ec.pipeline().addLast(new IntegerProcessHandler());
            }
        };
        EmbeddedChannel emChannel = new EmbeddedChannel(initializer);
        for (int i = 0; i < 100; i++) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(i);
            emChannel.writeAndFlush(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EmbeddedChannel emChannel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ec) throws Exception {
                ec.pipeline().addLast(new Byte2IntegerDecoder());
                ec.pipeline().addLast(new IntegerProcessHandler());
            }
        });
        for (int i = 0; i < 100; i++) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(i);
            emChannel.writeAndFlush(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
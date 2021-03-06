package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
public class IntegerProcessHandlerTest {
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
            emChannel.writeInbound(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
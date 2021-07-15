package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class StringReplayDecoderTest {

    @Test
    void decode() {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline()
                        .addLast(new StringReplayDecoder())
                        .addLast(new StringProcessHandler());
            }
        });

        String content = "Netty是一个高并发的框架";
        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
        // Random random = new Random();
        for (int i = 0; i < 100; i++) {
            // 1~3之间的整数
            int r = (int) (Math.random() * 3 + 1);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(bytes.length * r);
            for (int j = 0; j < r; j++) {
                buffer.writeBytes(bytes);
            }
            channel.writeInbound(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i<20; i++) {
            int r = (int) (Math.random() * 3 + 1);
            System.out.println(r);
        }
    }
}
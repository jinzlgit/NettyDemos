package com.jinzl.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Integer2ByteEncoderTest {

    @Test
    void encode() {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline()
                        .addLast(new Integer2ByteEncoder());
            }
        });

        for (int i = 0; i < 100; i++) {
            channel.write(i);
        }
        channel.flush();
        // 取得通道的出站数据包
        ByteBuf byteBuf = channel.readOutbound();
        while (byteBuf != null) {
            System.out.println("out=" + byteBuf.readInt());
            byteBuf = channel.readOutbound();
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
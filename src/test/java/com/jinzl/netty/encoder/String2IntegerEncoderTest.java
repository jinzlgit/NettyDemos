package com.jinzl.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试字符串到整数的编码器
 */
public class String2IntegerEncoderTest {
    @Test
    public void test() throws InterruptedException {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline()
                        .addLast(new Integer2ByteEncoder())
                        .addLast(new String2IntegerEncoder());
            }
        });

        String s = null;
        for (int i = 0; i < 100; i++) {
            s = "i am " + i;
            channel.write(s);
        }
        channel.flush();

        ByteBuf byteBuf = channel.readOutbound();
        while (byteBuf != null) {
            System.out.println("out=" + byteBuf.readInt());
            byteBuf = channel.readOutbound();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
}
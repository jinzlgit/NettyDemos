package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Zhenlin Jin
 * @date 2021/7/14 16:29
 */
public class IntegerAddDecoderTest {

    @Test
    public void testByte2IntegerReplayDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ec) throws Exception {
                ec.pipeline()
                        .addLast(new IntegerAddDecoder())
                        .addLast(new IntegerProcessHandler());
            }
        });
        for (int i = 0; i < 100; i++) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(i);
            channel.writeInbound(buffer);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

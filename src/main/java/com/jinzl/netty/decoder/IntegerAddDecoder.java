package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 整数两两相加解码器
 *
 * @author Zhenlin Jin
 * @date 2021/7/14 15:57
 */
public class IntegerAddDecoder extends ReplayingDecoder<IntegerAddDecoder.Status> {

    enum Status {
        PARSE_1, PARSE_2;
    }

    private int first;
    private int second;

    public IntegerAddDecoder() {
        // 在构造器函数中，需要初始化父类的state属性，表示当前阶段
        super(Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        switch (state()) {
            case PARSE_1:
                // 从装饰器 ByteBuf中读取数据
                first = byteBuf.readInt();
                // 第一步解析成功，
                // 进入第二步，并且设置“读断点指针”为当前的读取位置
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                second = byteBuf.readInt();
                int sum = first + second;
                list.add(sum);
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}

package com.jinzl.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 字符串的分包解码器
 *
 * @author Zhenlin Jin
 * @date 2021/7/15 9:06
 */
public class StringReplayDecoder extends ReplayingDecoder<StringReplayDecoder.Status> {
    /**
     * 每一个上层Header-Content内容的读取阶段
     */
    public enum Status {
        PARSE_1,
        PARSE_2;
    }

    private int length;
    private byte[] inBytes;

    public StringReplayDecoder() {
        super(Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        switch (state()) {
            case PARSE_1:
                // 第一步：从装饰器ByteBuf中读取长度
                length = byteBuf.readInt();
                inBytes = new byte[length];
                // 进入第二步：读取内容
                // 并且设置“读断点指针”为当前的读取位置，同时设置下一个阶段
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                // 第二步：从装饰器ByteBuf中读取内容数组
                byteBuf.readBytes(inBytes,0, length);
                list.add(new String(inBytes, "UTF-8"));
                // 第二步解析成功，
                // 进入第一步，读取下一个字符串的长度
                // 并且设置“读断点指针”为当前的读取位置，同时设置下一个阶段
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}

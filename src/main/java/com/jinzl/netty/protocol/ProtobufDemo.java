package com.jinzl.netty.protocol;

/**
 * proto序列化示例
 *
 * @author Zhenlin Jin
 * @date 2021/7/15 15:51
 */
public class ProtobufDemo {
    public static MsgProtos.Msg buildMsg() {
        MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
        builder.setId(1000);
        builder.setContent("哈哈哈哈哈哈");
        MsgProtos.Msg message = builder.build();
        return message;
    }

}

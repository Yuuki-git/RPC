package com.yk.serializer;

import com.yk.entity.RPCRequest;
import com.yk.entity.RPCResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.io.Serializable;

/**
 * @author yk
 * @version 1.0
 * @date 2021/8/6 16:53
 */
@AllArgsConstructor
public class MyEncode extends MessageToByteEncoder {
    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
//        System.out.println(o.getClass());
        // 写入消息类型
        if(o instanceof RPCRequest){
            byteBuf.writeShort(0);
        } else if(o instanceof RPCResponse){
            byteBuf.writeShort(1);
        }
        //写入序列化类型
        byteBuf.writeShort(serializer.getType());
        //得到序列化后的数组
        byte[] bytes = serializer.serialize(o);
        //写入数组长度
        byteBuf.writeInt(bytes.length);
        //写入数组
        byteBuf.writeBytes(bytes);
    }
}

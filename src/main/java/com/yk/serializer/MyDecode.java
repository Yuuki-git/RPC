package com.yk.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @date 2021/8/6 17:08
 */
public class MyDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //1.读取消息类型
        short messageType = byteBuf.readShort();
        if(messageType != 0 && messageType != 1){
            System.out.println("暂不支持此种数据");
            return;
        }
        //读取序列化类型
        short serializerType = byteBuf.readShort();
        //得到对应的序列化器
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        if(serializer == null) {
            throw new RuntimeException("不存在对应的序列化器");
        }
        //读取序列化数组长度
        int length = byteBuf.readInt();
        //读取序列化数组
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        //使用序列化器解码(反序列化)
        Object obj = serializer.deSerialize(bytes, messageType);
        list.add(obj);

    }
}

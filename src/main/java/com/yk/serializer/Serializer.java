package com.yk.serializer;


/**
 * @author yk
 * @version 1.0
 * @date 2021/8/5 16:54
 */
public interface Serializer {
    /**把对象序列化成字节数组*/
    byte[] serialize(Object obj);
    /**
     * 用不同方式把字节数组反序列化为对象
     */
    Object deSerialize(byte[] bytes,int type);
    /**根据序号取出序列化器
     * type=0:java自带序列化方式
     * type=1:json序列化方式
     * */
    static Serializer getSerializerByCode(int code){
        switch (code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

    int getType();

}

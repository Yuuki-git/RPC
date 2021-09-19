package com.yk.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yk.entity.RPCRequest;
import com.yk.entity.RPCResponse;

/**
 * @author yk
 * @version 1.0
 * @date 2021/8/5 17:09
 */
public class JsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    @Override
    public Object deSerialize(byte[] bytes, int type) {
        Object obj = null;
        switch (type){
            case 0:
                RPCRequest request = JSONObject.parseObject(bytes,RPCRequest.class);
                if(request.getParams() == null) {
                    return request;
                }
                Object[] objects = new Object[request.getParams().length];
                // 把json字串转化成对应的对象， fastjson可以读出基本数据类型，不用转化
                for (int i = 0; i < objects.length; i++) {
                     Class<?> paramsType = request.getParamsTypes()[i];
                     if(!paramsType.isAssignableFrom(request.getParams()[i].getClass())){
                         //如果类型不同,进行类型转换
                         /*
                         * java.lang.Integer cannot be cast to com.alibaba.fastjson.JSONObject
                         * 包装类型无法转换?
                         * */
                         objects[i] = JSONObject.toJavaObject((JSONObject) request.getParams()[i],request.getParamsTypes()[i]);
                     } else {
                         objects[i] = request.getParams()[i];
                     }
                }
                request.setParams(objects);
                obj = request;
                break;
            case 1:
                RPCResponse response = JSON.parseObject(bytes, RPCResponse.class);
                Class<?> dataType = response.getDataType();
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    //类型转换
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(),dataType));
                }
                obj = response;
                break;
            default:
                System.out.println("暂时不支持此种消息");
                throw new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}

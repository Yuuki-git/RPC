package com.yk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务端返回Response响应
 *
 * 将传输对象抽象成为Object
 * 引入状态码和状态信息表示服务调用成功还是失败
 *
 * @author yk
 * @version 1.0
 * @date 2021/7/18 15:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RPCResponse implements Serializable {
    /**状态信息*/
    private int code;
    private String message;
    /**待传输数据*/
    private Object data;
    /**数据类型*/
    private Class<?> dataType;


    public RPCResponse(int code, Object data) {
        this.code = code;
        this.data = data;
        this.dataType = data.getClass();
    }

    /**成功*/
    public static RPCResponse success(Object data){
        return new RPCResponse(200,data);
    }
    /**失败*/
    public static RPCResponse fail(){
        return new RPCResponse(500,"服务器错误!");
    }


}

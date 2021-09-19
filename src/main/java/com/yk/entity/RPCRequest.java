package com.yk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户端发送Request请求
 *
 * 一个RPC请求中，client发送应该是需要调用的Service接口名，方法名，参数，参数类型
 * 这样服务端就能根据这些信息根据反射调用相应的方法
 * 还是使用java自带的序列化方式
 *
 * @author yk
 * @version 1.0
 * @date 2021/7/18 15:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RPCRequest implements Serializable {
    /**服务类名*/
    private String interfaceName;
    /**方法名*/
    private String methodName;
    /**参数列表*/
    private Object[] params;
    /**参数类型*/
    private Class<?>[] paramsTypes;

}

package com.yk.proxy;

import com.yk.client.RPCClient;
import com.yk.entity.RPCRequest;
import com.yk.entity.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/18 16:19
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {

    /**通过构造器初始化*/
    String host;
    int port;
    RPCClient rpcClient;

    public ClientProxy(RPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建Request对象
        RPCRequest request = new RPCRequest(method.getDeclaringClass().getName(), method.getName(),args, method.getParameterTypes());
        //数据传输
        RPCResponse response = rpcClient.sendRequest(request);


        if(response == null){
            System.out.println("服务端未启动!");
            return null;
        }
        //无法保证response能拿到数据
        return response.getData();
    }
    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}

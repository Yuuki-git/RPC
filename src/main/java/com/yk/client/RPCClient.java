package com.yk.client;

import com.yk.entity.RPCRequest;
import com.yk.entity.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


 /**
  *  * @author yk
  *  * @version 1.0
  *  * @date 2021/7/18 16:08
  *这里负责底层与服务端的通信，发送的Request，接受的是Response对象
  *客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
  *这里的request是封装好的（上层进行封装），不同的service需要进行不同的封装， 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service
  */
public interface RPCClient {

    RPCResponse sendRequest(RPCRequest request);

}

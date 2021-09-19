package com.yk.client;
import com.yk.entity.RPCRequest;
import com.yk.entity.RPCResponse;
import com.yk.entity.User;
import com.yk.proxy.ClientProxy;
import com.yk.register.ServiceRegister;
import com.yk.register.impl.ZkServiceRegister;
import com.yk.service.TestService;
import com.yk.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * v1:动态代理
 * @author yk
 * @version 1.0
 * @date 2021/7/18 13:49
 */
@AllArgsConstructor
@Data
public class SimpleRPCClient implements RPCClient{

    private String host;
    private int port;

    private ServiceRegister serviceRegister;

    public SimpleRPCClient() {
        // 初始化注册中心，建立连接
        this.serviceRegister = new ZkServiceRegister();
    }

    public static void main(String[] args) {

        // 构建一个使用java Socket传输的客户端
        SimpleRPCClient simpleRPCClient = new SimpleRPCClient();
        // 把这个客户端传入代理客户端
        ClientProxy clientProxy = new ClientProxy(simpleRPCClient);

        // 代理客户端根据不同的服务，获得一个代理类， 并且这个代理类的方法以或者增强（封装数据，发送请求）
        UserService userService = clientProxy.getProxy(UserService.class);
        TestService testService = clientProxy.getProxy(TestService.class);
        //调用方法1
        long startTime=System.currentTimeMillis();
        User user = userService.getUserByUserId(111);
        long endTime=System.currentTimeMillis();
        System.out.println("user:从服务端得到的user:"+user+",消耗时间为:"+(endTime-startTime)+"ms");

        startTime=System.currentTimeMillis();
        userService.insertUserId(new User(1,"yk",true));
        endTime=System.currentTimeMillis();
        System.out.println("user:向服务端插入数据:"+user+",消耗时间为:"+(endTime-startTime)+"ms");
        //调用方法2
        startTime=System.currentTimeMillis();
        String result = testService.test(1);
        endTime=System.currentTimeMillis();
        System.out.println("test:"+result+",消耗时间为:"+(endTime-startTime)+"ms");

    }

    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        try {
            //从注册中心获取host,port
            InetSocketAddress inetSocketAddress = serviceRegister.serviceDiscovery(request.getInterfaceName());
            host = inetSocketAddress.getHostString();
            port = inetSocketAddress.getPort();
            Socket socket = new Socket(host, port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println(request);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            return (RPCResponse) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}

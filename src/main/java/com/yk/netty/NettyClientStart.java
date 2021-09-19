package com.yk.netty;

import com.yk.entity.User;
import com.yk.proxy.ClientProxy;
import com.yk.service.TestService;
import com.yk.service.UserService;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/23 16:24
 */
public class NettyClientStart {

    //启动netty
    public static void main(String[] args) {
        // 构建一个使用netty传输的客户端
        NettyRPCClient nettyRPCClient = new NettyRPCClient();

        // 把这个客户端传入代理客户端
        ClientProxy clientProxy = new ClientProxy(nettyRPCClient);

        // 代理客户端根据不同的服务，获得一个代理类， 并且这个代理类的方法以或者增强（封装数据，发送请求）
        UserService userService = clientProxy.getProxy(UserService.class);
        TestService testService = clientProxy.getProxy(TestService.class);
        //调用方法1
        long startTime=System.currentTimeMillis();
        User user = userService.getUserByUserId(111);
        long endTime=System.currentTimeMillis();
        System.out.println("user:从服务端得到的user:"+user+",消耗时间为:"+(endTime-startTime)+"ms");

        startTime=System.currentTimeMillis();
        user = new User(1,"ykk",true);
        userService.insertUserId(user);
        endTime=System.currentTimeMillis();
        System.out.println("user:向服务端插入数据:"+user+",消耗时间为:"+(endTime-startTime)+"ms");
        //调用方法2
        startTime=System.currentTimeMillis();
        String result = testService.test(1);
        endTime=System.currentTimeMillis();
        System.out.println("test:"+result+",消耗时间为:"+(endTime-startTime)+"ms");
    }
}

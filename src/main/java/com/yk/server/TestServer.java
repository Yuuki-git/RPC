package com.yk.server;

import com.yk.entity.ServiceProvider;
import com.yk.server.impl.SimpleRPCServer;
import com.yk.service.TestService;
import com.yk.service.UserService;
import com.yk.service.impl.TestServiceImpl;
import com.yk.service.impl.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/20 16:01
 */
public class TestServer {

    public static void main(String[] args) {
//        Map<String,Object> serviceProvide = new HashMap<>();

        TestService testService = new TestServiceImpl();
        UserService userService = new UserServiceImpl();

        //将接口名和service对象存入Map
        /*serviceProvide.put(userService.getClass().getName(),userService);
        serviceProvide.put(testService.getClass().getName(),testService);*/

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1",8899);
        serviceProvider.put(testService);
        serviceProvider.put(userService);
        /*serviceProvider.put(new UserServiceImpl());
        serviceProvider.put(new TestServiceImpl());*/


        RPCServer simpleServer = new SimpleRPCServer(serviceProvider);
        simpleServer.start(8899);
    }




}

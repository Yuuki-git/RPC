package com.yk.netty;

import com.yk.entity.ServiceProvider;
import com.yk.server.RPCServer;
import com.yk.service.impl.TestServiceImpl;
import com.yk.service.impl.UserServiceImpl;

/**
 * @author yk
 * @version 1.0
 * @date 2021/8/13 17:00
 */
public class NettyServerStart2 {


    public static void main(String[] args) {
        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1",8898);
        serviceProvider.put(new UserServiceImpl());
        serviceProvider.put(new TestServiceImpl());

        RPCServer nettyServer = new NettyRPCServer(serviceProvider);
        nettyServer.start(8898);
    }
}

package com.yk.server.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yk.entity.ServiceProvider;
import com.yk.server.RPCServer;
import com.yk.server.WorkThread;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/20 16:02
 * 这个实现类代表着java原始的BIO监听模式，来一个任务，就new一个线程去处理
 */
@AllArgsConstructor
public class SimpleRPCServer implements RPCServer {

    /**存放服务接口名和 service 对象*/
//    Map<String, Object> serviceProvide;
    ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        try {

            //自定义线程池
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("demo-pool-%d").build();
            ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 10,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动成功");
            //BIO的方式监听Socket
            while(true){
                final Socket socket = serverSocket.accept();
                //开启一个线程
                singleThreadPool.execute(new WorkThread(serviceProvider,socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    @Override
    public void stop() {

    }
}

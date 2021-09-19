package com.yk.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yk.entity.RPCRequest;
import com.yk.entity.RPCResponse;
import com.yk.service.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/18 14:00
 */
public interface RPCServer {
    void start(int port);
    void stop();
    /*public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        try {

            //自定义线程池
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("demo-pool-%d").build();
            ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动");
            //BIO的方式监听Socket
            while(true){
                final Socket socket = serverSocket.accept();
                //开启一个线程
                singleThreadPool.execute(()->{
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        //读取客户端传来的Request
                        RPCRequest request = (RPCRequest) in.readObject();
                        //反射调用对应方法
                        Method method = userService.getClass().getMethod(request.getMethodName(),request.getParamsTypes());
                        Object invoke = method.invoke(userService,request.getParams());
                        //将Response对象封装后传回客户端
                        out.writeObject(RPCResponse.success(invoke));
                        out.flush();
                    } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                            IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        System.out.println("从IO中读取数据错误");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }*/

}

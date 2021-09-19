package com.yk.server;

import com.yk.entity.RPCRequest;
import com.yk.entity.RPCResponse;
import com.yk.entity.ServiceProvider;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/20 16:53
 *  这里负责解析得到的request请求，执行服务方法，返回给客户端
 *  1.从request得到interfaceName 2. 根据interfaceName在serviceProvide Map中获取服务端的实现类
 *  3. 从request中得到方法名，参数， 利用反射执行服务中的方法 4. 得到结果，封装成response，写入socket
 *
 */
@AllArgsConstructor
public class WorkThread implements Runnable{
    ServiceProvider serviceProvider;
    Socket socket;

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            //得到request
            RPCRequest request = (RPCRequest) in.readObject();
            //得到service对象
            Object service = serviceProvider.get(request.getInterfaceName());
            //反射得到method方法
            Method method = service.getClass().getMethod(request.getMethodName(),request.getParamsTypes());
            //调用method方法
            Object result = method.invoke(service, request.getParams());
            //封装成response传回客户端
            out.writeObject(RPCResponse.success(result));
            out.flush();

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("从IO中读取数据错误");
        }
    }
}

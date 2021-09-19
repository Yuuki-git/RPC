package com.yk.entity;

import com.yk.register.ServiceRegister;
import com.yk.register.impl.ZkServiceRegister;

import javax.imageio.spi.ServiceRegistry;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/20 17:40
 *
 * 封装一个简单的map
 * <接口数组,service对象>
 */
public class ServiceProvider {
    /**
     * 一个实现类可能实现多个接口
     */
    private Map<Object, Object> interfaceProvider;
    private ServiceRegister serviceRegister;
    private String host;
    private int port;

    public ServiceProvider(String host,int port) {
        this.host = host;
        this.port = port;
        this.serviceRegister = new ZkServiceRegister();
        this.interfaceProvider = new HashMap<>();
    }

    public void put(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();


        for (Class clazz : interfaces) {
            interfaceProvider.put(clazz.getName(), service);
            //把每一个 service 注册到注册中心
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port));
        }

    }
    public Object get(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}

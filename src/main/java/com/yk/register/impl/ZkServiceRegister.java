package com.yk.register.impl;

import com.yk.loadBalance.LoadBalance;
import com.yk.loadBalance.impl.RoundLoadBalance;
import com.yk.register.ServiceRegister;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @date 2021/8/12 16:44
 */
public class ZkServiceRegister implements ServiceRegister {
    private CuratorFramework client;
    // zookeeper根路径节点
//    private static final String ROOT_PATH = "MyRPC";
    private LoadBalance loadBalance;
    public ZkServiceRegister() {
        this.loadBalance = new RoundLoadBalance();
        //对zk客户端初始化

        /*retryPolicy：失败重试策略
        ExponentialBackoffRetry：构造器含有三个参数 ExponentialBackoffRetry(int baseSleepTimeMs, int maxRetries, int maxSleepMs)
        baseSleepTimeMs：初始的sleep时间，用于计算之后的每次重试的sleep时间，
        计算公式：当前sleep时间=baseSleepTimeMs*Math.max(1, random.nextInt(1<<(retryCount+1)))
        maxRetries：最大重试次数
        maxSleepMs：最大sleep时间，如果上述的当前sleep计算出来比这个大，那么sleep用这个时间*/

        //connectString：zk的server地址，多个server之间使用英文逗号分隔开
        //connectionTimeoutMs：连接超时时间
        //sessionTimeoutMs：会话超时时间
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3 );
        this.client = CuratorFrameworkFactory.builder().sessionTimeoutMs(40000)
                .namespace("MyRPC")
                .connectString("127.0.0.1:2181").retryPolicy(retryPolicy).build();

        this.client.start();
        System.out.println("zookeeper 连接成功");
    }

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        // 创建一个名为 serviceName 的永久节点，服务提供者下线时，不删服务名，只删地址
        try {
            if(client.checkExists().forPath("/"+serviceName) == null){
                //如果当前服务名不存在,则创建,否则抛出异常
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/"+serviceName);
            }
            //一个 / 代表一个节点
            String path = "/" + serviceName + "/" + getServiceAddress(serverAddress);
            //临时节点 服务器下线自动删除
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            System.out.println("此服务已存在!");
        }
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> stringList = client.getChildren().forPath("/" + serviceName);
            //默认选第一个
            //String s = stringList.get(0);
            String s = loadBalance.balance(stringList);
            return parseAddress(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     /**地址 -> XXX.XXX.XXX.XXX:port 字符串*/
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName()+":"+serverAddress.getPort();
    }
    /** 字符串解析为地址*/
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}

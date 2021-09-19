package com.yk.netty;

import com.yk.client.RPCClient;
import com.yk.client.SimpleRPCClient;
import com.yk.entity.RPCRequest;
import com.yk.entity.RPCResponse;
import com.yk.entity.User;
import com.yk.proxy.ClientProxy;
import com.yk.register.ServiceRegister;
import com.yk.register.impl.ZkServiceRegister;
import com.yk.service.TestService;
import com.yk.service.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/23 15:26
 */
public class NettyRPCClient implements RPCClient {

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private ServiceRegister serviceRegister;
    private String host;
    private int port;
    public NettyRPCClient() {
        //初始化注册中心
        this.serviceRegister = new ZkServiceRegister();
    }
    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回， 而不是想要的相应的response
     */
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        try {
            //通过zk获取host port
            InetSocketAddress inetSocketAddress = serviceRegister.serviceDiscovery(request.getInterfaceName());
            host = inetSocketAddress.getHostName();
            port = inetSocketAddress.getPort();
            ChannelFuture channelFuture  = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题
            // 实际上不应通过阻塞，可通过回调函数
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}

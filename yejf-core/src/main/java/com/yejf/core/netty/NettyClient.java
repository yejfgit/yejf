package com.yejf.core.netty;

import com.yejf.core.netty.code.RpcDecoder;
import com.yejf.core.netty.code.RpcEncoder;
import com.yejf.core.netty.handler.ClientHandler;
import com.yejf.core.netty.model.RpcRequest;
import com.yejf.core.netty.model.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author yejf
 * @date 2019/4/4 17:19
 */
@Slf4j
public class NettyClient {
    private Channel channel;
    public Channel getChannel() {
        return channel;
    }

    public void start(String host,int port) throws Exception {
        final EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        // 使用NioSocketChannel来作为连接用的channel类
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 绑定连接初始化器
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        log.info("正在连接中...");
                        ChannelPipeline pipeline = ch.pipeline();
                        //编码request
                        pipeline.addLast(new RpcEncoder(RpcRequest.class));
                        //解码response
                        pipeline.addLast(new RpcDecoder(RpcResponse.class));
                        //客户端处理类
                        pipeline.addLast(new ClientHandler());


                    }
                });
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = b.connect(host, port).sync();

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    log.info("连接服务器成功");
                } else {
                    log.info("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });

        this.channel = future.channel();

    }

    public static void main(String[] args) throws Exception {
        NettyClient nettyClient = new NettyClient();
        String host = "127.0.0.1";
        int port = 8888;
        nettyClient.start(host,port);

        //Thread.sleep(5000);

        Channel channel = nettyClient.getChannel();
        //消息体
        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setData("client1.message");
        //channel对象可保存在map中，供其它地方发送消息
        channel.writeAndFlush(request);


        NettyClient nettyClient2 = new NettyClient();
        nettyClient2.start(host,port);

        Channel channel2 = nettyClient.getChannel();
        //消息体
        RpcRequest request2 = new RpcRequest();
        request2.setId(UUID.randomUUID().toString());
        request2.setData("client2.message");
        //channel对象可保存在map中，供其它地方发送消息
        channel2.writeAndFlush(request2);
    }

}

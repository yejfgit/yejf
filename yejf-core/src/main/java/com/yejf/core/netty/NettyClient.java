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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
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
            ChannelFuture future = null;
            try {
                future = b.connect(host, port).sync();
            } catch (InterruptedException e) {
                log.error("NettyClient ChannelFuture InterruptedException",e);
            }

            ChannelFuture finalFuture = future;
            future.addListener((ChannelFutureListener) arg0 -> {
                if (finalFuture.isSuccess()) {
                    log.info("连接服务器成功");
                } else {
                    log.info("连接服务器失败");
                    finalFuture.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            });

            this.channel = future.channel();
        });

    }

}

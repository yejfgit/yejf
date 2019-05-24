package com.yejf.core.netty;

import com.yejf.core.netty.code.RpcDecoder;
import com.yejf.core.netty.code.RpcEncoder;
import com.yejf.core.netty.handler.ServerHandler;
import com.yejf.core.netty.model.RpcRequest;
import com.yejf.core.netty.model.RpcResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * netty服务类
 * @author yejf
 * @date 2019/5/23 19:16
 */
@Slf4j
public final class NettyServer {

    public void start(int port) throws Exception{
        //bossGroup就是parentGroup，是负责处理TCP/IP连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //workerGroup就是childGroup,是负责处理Channel(通道)的I/O事件
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //初始化服务端可连接队列,指定了队列的大小128
                .option(ChannelOption.SO_BACKLOG, 128)
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 绑定客户端连接时候触发操作
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sh) throws Exception {
                        sh.pipeline()
                                //编码response
                                .addLast(new RpcEncoder(RpcResponse.class))
                                //解码request
                                .addLast(new RpcDecoder(RpcRequest.class))
                                //使用ServerHandler类来处理接收到的消息
                                .addLast(new ServerHandler());
                    }
                });
        //绑定监听端口，调用sync同步阻塞方法等待绑定操作完
        ChannelFuture future = sb.bind(port).sync();

        if (future.isSuccess()) {
            log.info("服务端启动成功");
        } else {
            log.info("服务端启动失败");
            future.cause().printStackTrace();
            bossGroup.shutdownGracefully(); //关闭线程组
            workerGroup.shutdownGracefully();
        }
        //成功绑定到端口之后,给channel增加一个 管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程。
        future.channel().closeFuture().sync();
    }
}

package com.yejf.web.config;

import com.yejf.core.netty.NettyClient;
import com.yejf.core.netty.NettyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yejf
 * @date 2019/5/23 19:13
 */
@Configuration
public class NettyConfig {

    @Value("${netty.server.port}")
    private Integer port;

    @Bean(initMethod = "start")
    public NettyServer nettyServer() throws Exception {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(port);
        return nettyServer;
    }

    @Bean(initMethod = "start")
    public NettyClient nettyClient() throws Exception {
        NettyClient nettyClient = new NettyClient();
        nettyClient.start("127.0.0.1",port);
        return nettyClient;
    }
}

package com.yejf.web.config;

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

    @Bean
    public NettyServer nettyServer() throws Exception {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(port);
        return nettyServer;
    }

}

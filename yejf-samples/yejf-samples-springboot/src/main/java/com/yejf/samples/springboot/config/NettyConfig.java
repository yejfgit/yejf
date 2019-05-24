package com.yejf.samples.springboot.config;

import com.yejf.core.netty.NettyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yejf
 * @date 2019/5/23 19:13
 */
@Configuration
public class NettyConfig {

    @Value("${netty.server.host}")
    private String host;
    @Value("${netty.server.port}")
    private Integer port;

    @Bean
    public NettyClient nettyClient() throws Exception {
        NettyClient nettyClient = new NettyClient();
        nettyClient.start(host,port);
        return nettyClient;
    }

}

package com.yejf.samples.springboot.controller;

import com.yejf.core.netty.NettyClient;
import com.yejf.core.netty.model.RpcRequest;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author yejf
 * @date 2019/5/29 11:46
 */
@RestController
public class TestController {

    @Autowired
    private NettyClient nettyClient;

    @GetMapping("/test")
    public String test(){
        Channel channel = nettyClient.getChannel();
        //消息体
        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setData("client1.message");
        //channel对象可保存在map中，供其它地方发送消息
        channel.writeAndFlush(request);
        return "yejf-samples-springboot";
    }
}

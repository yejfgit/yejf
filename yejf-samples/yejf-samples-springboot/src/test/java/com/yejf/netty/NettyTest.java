package com.yejf.netty;

import com.yejf.BaseTest;
import com.yejf.core.netty.NettyClient;
import com.yejf.core.netty.model.RpcRequest;
import io.netty.channel.Channel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author hzyejinfu
 * @date 2019/5/24 17:03
 */
public class NettyTest extends BaseTest {

    @Autowired
    private NettyClient nettyClient;

    @Test
    public void sendMsgTest() throws InterruptedException {
        Channel channel = nettyClient.getChannel();
        //消息体
        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setData("client1.message");
        //channel对象可保存在map中，供其它地方发送消息
        channel.writeAndFlush(request);

        Thread.sleep(5000);

    }
}

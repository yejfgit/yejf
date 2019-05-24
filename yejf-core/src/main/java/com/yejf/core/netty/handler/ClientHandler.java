package com.yejf.core.netty.handler;

import com.yejf.core.netty.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yejf
 * @date 2019/5/23 11:54
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    /**
     * 处理服务端返回的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        log.info("接受到server响应数据: " + response.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}

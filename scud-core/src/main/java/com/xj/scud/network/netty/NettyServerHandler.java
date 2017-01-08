package com.xj.scud.network.netty;

import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.server.ServerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/02 14:25
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<NetworkProtocol> {
    private ServerManager manager;

    public NettyServerHandler(ServerManager manager) {
        this.manager = manager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NetworkProtocol msg) throws Exception {
        manager.invoke(msg, ctx);
    }
}

package com.xj.scud.core.network.netty;

import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.server.ServerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Author: xiajun
 * Date: 2017/01/02 14:25
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<NetworkProtocol> {
    private final static Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);
    private ServerManager manager;

    public NettyServerHandler(ServerManager manager) {
        this.manager = manager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NetworkProtocol msg){
        if (msg.getType() == -1) {//心跳包
            ctx.writeAndFlush(msg);
        } else {
            manager.invoke(msg, ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String clientIP = getClientIp(ctx);
        String message = cause.getMessage();
        if (message != null && message.contains("Connection reset by peer")) {
            LOGGER.error("client IP:{} " + message, clientIP);
        } else {
            LOGGER.error("client IP:{} {} ", message, clientIP, cause);
        }
    }

    private String getClientIp(ChannelHandlerContext ctx) {
        String ip;
        try {
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            ip = socketAddress.getAddress().getHostAddress();
        } catch (Exception e) {
            ip = "unknown";
        }
        return ip;
    }
}

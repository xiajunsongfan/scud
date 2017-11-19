package com.xj.scud.core.network.netty;

import com.xj.scud.core.MessageManager;
import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.RpcResult;
import com.xj.scud.core.network.SerializableHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author: xiajun
 * Date: 2017/01/02 14:35
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<NetworkProtocol> {
    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);

    public NettyClientHandler() {
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final NetworkProtocol msg) throws Exception {
        if (msg.getType() == -1) {
            LOGGER.debug("Client recv heart package id={}", msg.getSequence());
        } else {
            try {
                RpcResult result = SerializableHandler.responseDecode(msg);
                MessageManager.release(msg.getSequence(), result);
            } catch (Exception e) {
                LOGGER.error("Client handler fail.", e);
            }
        }
    }
}

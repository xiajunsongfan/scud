package com.xj.scud.network.netty;

import com.xj.scud.core.MessageManager;
import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.RpcResult;
import com.xj.scud.network.SerializableHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/02 14:35
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<NetworkProtocol> {
    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);
    private ThreadPoolExecutor executor;

    public NettyClientHandler(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final NetworkProtocol msg) throws Exception {
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcResult result = SerializableHandler.responseDecode(msg);
                    MessageManager.release(msg.getSequence(), result);
                } catch (Exception e) {
                    LOGGER.error("Client handler fail.", e);
                }
            }
        });
    }

}

package com.xj.scud.clent;

import com.xj.scud.core.MessageManager;
import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.ResponseFuture;
import com.xj.scud.core.RpcResult;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/08 12:00
 */
public class ClientInvoker<T> implements Invoker<T> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientInvoker.class);
    private ClientConfig config;

    public ClientInvoker(ClientConfig config) {
        this.config = config;
    }

    @Override
    public T invoke(final Channel ch, final NetworkProtocol protocol) throws Exception {
        ResponseFuture<RpcResult> future = MessageManager.setSeq(protocol.getSequence());
        ChannelFuture channelFuture = ch.writeAndFlush(protocol);
        if (LOGGER.isDebugEnabled()) {
            final long startTime = System.currentTimeMillis();
            channelFuture.addListeners(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    LOGGER.debug("Scud send msg packageId={} cost {}ms, exception= {}", protocol.getSequence(), (System.currentTimeMillis() - startTime), future.cause());
                }
            });
        }
        RpcResult result = future.get(config.getTimeout(), TimeUnit.MILLISECONDS);
        if (result == null) {//客户端超时
            MessageManager.remove(protocol.getSequence());
        }
        return (T) result;
    }
}

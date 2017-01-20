package com.xj.scud.clent;

import com.xj.scud.core.NetworkProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/08 12:00
 */
public class ClientInvoker implements Invoker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientInvoker.class);

    @Override
    public void invoke(final Channel ch, final NetworkProtocol protocol) throws Exception {
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
    }
}

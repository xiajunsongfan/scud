package com.xj.scud.clent;

import com.xj.scud.core.MessageManager;
import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.ResponseFuture;
import com.xj.scud.core.RpcResult;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/08 12:00
 */
public class ClientInvoker<T> implements Invoker<T> {
    private ClientConfig config;

    public ClientInvoker(ClientConfig config) {
        this.config = config;
    }

    @Override
    public T invoke(Channel ch, NetworkProtocol protocol) throws Exception {
        ResponseFuture<RpcResult> future = MessageManager.setSeq(protocol.getSequence());
        ch.writeAndFlush(protocol);
        RpcResult result = future.get(config.getTimeout(), TimeUnit.MILLISECONDS);
        if (result == null) {//客户端超时
            MessageManager.remove(protocol.getSequence());
        }
        return (T) result;
    }
}

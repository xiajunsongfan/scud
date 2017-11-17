package com.xj.scud.client;

import com.xj.scud.core.NetworkProtocol;
import io.netty.channel.Channel;

/**
 * Author: xiajun
 * Date: 2017/01/08 10:21
 */
public interface Invoker {
    void invoke(Channel ch, NetworkProtocol protocol,int packageId) throws Exception;
}

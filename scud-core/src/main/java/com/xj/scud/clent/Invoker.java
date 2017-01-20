package com.xj.scud.clent;

import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.RpcFuture;
import io.netty.channel.Channel;

/**
 * Author: xiajun
 * Date: 2017/01/08 10:21
 */
public interface Invoker {
    void invoke(Channel ch, NetworkProtocol protocol) throws Exception;
}

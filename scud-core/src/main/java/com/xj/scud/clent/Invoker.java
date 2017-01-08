package com.xj.scud.clent;

import com.xj.scud.core.NetworkProtocol;
import io.netty.channel.Channel;

/**
 * Author: xiajun
 * Date: 2017/01/08 10:21
 */
public interface Invoker<T> {
    T invoke(Channel ch, NetworkProtocol protocol) throws Exception;
}

package com.xj.scud.core;

import java.util.concurrent.CompletableFuture;

/**
 * Author: xiajun
 * Date: 2017/01/20 10:46
 */
public abstract class RpcFuture<T> extends CompletableFuture<T> {
    protected long sendTime;//发送的时间
    protected int invokerTimeout;//调用超时

    public RpcFuture(long sendTime, int invokerTimeout) {
        this.sendTime = sendTime;
        this.invokerTimeout = invokerTimeout;
    }

    /**
     * 服务端响应消息后的回调
     *
     * @param result 响应数据
     */
    public abstract void responseReceived(RpcResult result);

    public long getSendTime() {
        return sendTime;
    }

    public int getInvokerTimeout() {
        return invokerTimeout;
    }

}

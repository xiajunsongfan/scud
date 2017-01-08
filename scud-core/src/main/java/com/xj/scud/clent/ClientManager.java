package com.xj.scud.clent;

import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.ProtocolProcesser;
import com.xj.scud.core.RpcResult;
import com.xj.scud.network.netty.NettyClient;
import io.netty.channel.Channel;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 18:32
 * 客户端管理类，后续可扩展为集群管理
 */
public class ClientManager<T> {
    private static AtomicInteger seqCount = new AtomicInteger(0);
    private ProtocolProcesser processer;
    private ClientConfig config;
    private Invoker<RpcResult> invoker;
    private volatile boolean init = false;
    private Channel ch;

    public ClientManager(ProtocolProcesser processer, ClientConfig config) {
        this.processer = processer;
        this.config = config;
        this.initChannel();
        invoker = new ClientInvoker(config);
    }

    /**
     * 初始化netty
     */
    public synchronized void initChannel() {
        if (!init) {
            this.ch = NettyClient.start(this.config);
            init = true;
        }
    }

    /**
     * 获取客户端连接通道
     *
     * @return Channel
     * @throws InterruptedException e
     */
    public Channel getChannel() throws InterruptedException {
        return this.ch;
    }

    /**
     * 调用远程方法
     *
     * @param method 方法对象
     * @param args   方法参数
     * @return T
     * @throws Exception e
     */
    public T invoke(Method method, Object[] args) throws Exception {
        int seq = seqCount.incrementAndGet();
        NetworkProtocol protocol = this.processer.buildRequestProtocol(method, args, seq);
        RpcResult result = this.invoker.invoke(this.ch, protocol);
        Throwable exception = result.getException();
        if (exception != null) {
            throw new Exception(exception);
        }
        return (T) result.getValue();
    }
}

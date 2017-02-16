package com.xj.scud.clent;

import com.xj.scud.core.*;
import com.xj.scud.network.netty.NettyClient;
import com.xj.scud.route.NodeInfo;
import com.xj.scud.route.RpcRoute;
import io.netty.channel.Channel;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: xiajun
 * Date: 2017/01/03 18:32
 * 客户端管理类，后续可扩展为集群管理
 */
public class ClientManager<T> {
    private static AtomicInteger seqCount = new AtomicInteger(0);
    private ProtocolProcesser processer;
    private ClientConfig config;
    private Invoker invoker;
    private NettyClient nettyClient;
    private RpcRoute route;

    public ClientManager(ProtocolProcesser processer, ClientConfig config) {
        this.processer = processer;
        this.config = config;
        invoker = new ClientInvoker();
        nettyClient = new NettyClient(config);
        route = config.getRoute().getRoute();
    }

    /**
     * 初始化netty
     */
    public Channel initChannel(String ip, int port) {
        return nettyClient.connect(ip, port);
    }

    /**
     * 初始化所有客户端连接
     */
    public void initCluster() {
        String hostStr = this.config.getHost();
        if (hostStr != null) {
            String[] hosts = hostStr.split(";");
            for (String host : hosts) {
                String[] ipPort = host.trim().split(":");
                String ip = ipPort[0];
                int port = Integer.parseInt(ipPort[1]);
                Channel ch = this.initChannel(ip, port);
                NodeInfo nodeInfo = new NodeInfo(ip, port);
                route.addServerNode(nodeInfo, ch);

            }
        }
    }

    /**
     * 获取客户端连接通道
     *
     * @return Channel
     * @throws InterruptedException e
     */
    public Channel getChannel() throws InterruptedException {
        return this.route.getServer();
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
        RpcFuture<RpcResult> rpcFuture = new ResponseFuture<>(config.getTimeout());
        MessageManager.setSeq(seq, rpcFuture);
        this.invoker.invoke(this.getChannel(), protocol, seq);
        RpcResult result = null;
        try {
            result = rpcFuture.get(config.getTimeout(), TimeUnit.MILLISECONDS);
        } finally {
            if (result == null) {//客户端超时
                MessageManager.remove(protocol.getSequence());
            }
        }
        if (result != null) {
            Throwable exception = result.getException();
            if (exception != null) {
                throw new Exception(exception);
            }
            return (T) result.getValue();
        }
        return null;
    }

    /**
     * 异步获取调用结果
     *
     * @param method 方法
     * @param args   参数
     * @return RpcFuture<T>
     * @throws Exception e
     */
    public RpcFuture<T> asyncFutureInvoke(Method method, Object[] args) throws Exception {
        int seq = createdPackageId();
        NetworkProtocol protocol = this.processer.buildRequestProtocol(method, args, seq);
        RpcFuture<RpcResult> rpcFuture = new ResponseFuture<>(config.getTimeout());
        MessageManager.setSeq(seq, rpcFuture);
        this.invoker.invoke(this.getChannel(), protocol, seq);
        return new ResultFuture<>(rpcFuture, seq);
    }

    /**
     * 异步获取调用结果
     *
     * @param method   方法
     * @param args     参数
     * @param callback 回调函数
     * @throws Exception e
     */
    public void asyncCallbackInvoke(Method method, Object[] args, RpcCallback callback) throws Exception {
        int seq = seqCount.incrementAndGet();
        NetworkProtocol protocol = this.processer.buildRequestProtocol(method, args, seq);
        RpcFuture<RpcResult> rpcFuture = new ResponseCallback(callback, config.getTimeout());
        MessageManager.setSeq(seq, rpcFuture);
        this.invoker.invoke(this.getChannel(), protocol, seq);
    }

    /**
     * 生成一个包ID
     *
     * @return int
     */
    public int createdPackageId() {
        return seqCount.incrementAndGet();
    }
}

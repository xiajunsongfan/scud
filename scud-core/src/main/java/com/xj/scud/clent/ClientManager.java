package com.xj.scud.clent;

import com.xj.scud.clent.route.NodeInfo;
import com.xj.scud.clent.route.RpcRoute;
import com.xj.scud.commons.Config;
import com.xj.scud.core.*;
import com.xj.scud.core.network.netty.NettyClient;
import com.xj.zk.ZkClient;
import com.xj.zk.ZkClientException;
import com.xj.zk.listener.Listener;
import io.netty.channel.Channel;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: xiajun
 * Date: 2017/01/03 18:32
 * 客户端管理类，后续可扩展为集群管理
 */
public class ClientManager<T> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientManager.class);
    private static AtomicInteger seqCount = new AtomicInteger(0);
    private ProtocolProcesser processer;
    private ClientConfig config;
    private Invoker invoker;
    private NettyClient nettyClient;
    private RpcRoute route;
    private ZkClient zkClient;

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
        List<String> childs = null;
        if (!this.config.isUseZk()) {
            String hostStr = this.config.getHost();
            if (hostStr != null) {
                String[] hosts = hostStr.split(";");
                childs = new ArrayList<>(hosts.length);
                for (String host : hosts) {
                    childs.add(host);
                }
            }
        } else {
            zkClient = new ZkClient(config.getZkHost(), 2000, 4000);
            String path = Config.DNS_PREFIX + config.getInterfaze().getName() + "/" + config.getVersion();
            zkClient.listenChild(path, new Listener() {
                @Override
                public void listen(String path, Watcher.Event.EventType eventType, byte[] data) throws ZkClientException, SocketException {
                    String[] pathArr = path.split("/");
                    String host = pathArr[pathArr.length - 1];
                    String[] ipPort = host.split(":");
                    NodeInfo nodeInfo = new NodeInfo(ipPort[0], Integer.parseInt(ipPort[1]));
                    if (eventType == Watcher.Event.EventType.NodeCreated) {
                        LOGGER.info("Get server node :{}", nodeInfo.getPath());
                        Channel ch = initChannel(nodeInfo.getIp(), nodeInfo.getPort());
                        route.addServerNode(nodeInfo, ch);
                        LOGGER.info("Connection server {} success.", nodeInfo.getPath());
                    } else if (eventType == Watcher.Event.EventType.NodeDeleted) {
                        Channel channel = route.removeServerNode(nodeInfo.getPath());
                        LOGGER.info("Delete server node :{}", nodeInfo.getPath());
                        nettyClient.close(channel);
                        LOGGER.info("Close server {} connection.", nodeInfo.getPath());
                    }
                }
            });
        }
        if (childs != null) {
            for (String host : childs) {
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
    public T invoke(String serviceName, Method method, Object[] args) throws Exception {
        int seq = createdPackageId();
        NetworkProtocol protocol = this.processer.buildRequestProtocol(serviceName, this.config.getVersion(), method, args, seq);
        RpcFuture<RpcResult> rpcFuture = new ResponseFuture<>(config.getTimeout());
        rpcFuture.setMethodName(method.getName());
        MessageManager.setSeq(seq, rpcFuture);
        this.invoker.invoke(this.getChannel(), protocol, seq);
        RpcResult result = null;
        try {
            result = rpcFuture.get(config.getTimeout(), TimeUnit.MILLISECONDS);
        } finally {
            if (result == null) {//客户端超时
                MessageManager.remove(protocol.getSequence());
            }
            rpcFuture.monitor();
        }
        if (result != null) {
            Throwable exception = result.getException();
            if (exception != null) {//服务端发生异常
                throw new Exception("Service provider exception.", exception);
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
    public RpcFuture<T> asyncFutureInvoke(String serviceName, Method method, Object[] args) throws Exception {
        int seq = createdPackageId();
        NetworkProtocol protocol = this.processer.buildRequestProtocol(serviceName, this.config.getVersion(), method, args, seq);
        RpcFuture<RpcResult> rpcFuture = new ResponseFuture<>(config.getTimeout());
        rpcFuture.setMethodName(method.getName());
        MessageManager.setSeq(seq, rpcFuture);
        this.invoker.invoke(this.getChannel(), protocol, seq);
        return new AsyncResponseFuture<>(rpcFuture, seq);
    }

    /**
     * 异步获取调用结果
     *
     * @param method   方法
     * @param args     参数
     * @param callback 回调函数
     * @throws Exception e
     */
    public void asyncCallbackInvoke(String serviceName, Method method, Object[] args, RpcCallback callback) throws Exception {
        int seq = createdPackageId();
        NetworkProtocol protocol = this.processer.buildRequestProtocol(serviceName, this.config.getVersion(), method, args, seq);
        RpcFuture<RpcResult> rpcFuture = new AsyncResponseCallback(callback, config.getTimeout());
        rpcFuture.setMethodName(method.getName());
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

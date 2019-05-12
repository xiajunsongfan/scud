package com.xj.scud.client;

import com.google.gson.Gson;
import com.xj.scud.client.route.NodeInfo;
import com.xj.scud.client.route.RpcRoute;
import com.xj.scud.commons.Config;
import com.xj.scud.core.*;
import com.xj.scud.core.exception.ScudExecption;
import com.xj.scud.core.network.netty.NettyClient;
import com.xj.zk.ZkClient;
import com.xj.zk.ZkClientException;
import com.xj.zk.listener.Listener;
import io.netty.channel.Channel;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
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
    private static volatile ZkClient zkClient;//只能有一个client，不允许一个运用中出现多个zookeeper集群

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
            if (zkClient == null || !zkClient.isConnection()) {
                synchronized (ClientManager.class) {
                    if (zkClient == null) {
                        zkClient = new ZkClient(config.getZkHost(), 2000, 4000);
                    }
                }
            }
            String path = Config.DNS_PREFIX + config.getInterfaze().getName() + "/" + config.getVersion();
            final String clientPath = path + "_clent";
            if (!zkClient.exists(clientPath)) {
                zkClient.create(clientPath, new byte[1]);
            }
            zkClient.listenChildData(path, new Listener() {
                @Override
                public void listen(String path, Watcher.Event.EventType eventType, byte[] data) throws ZkClientException, SocketException {
                    String[] pathArr = path.split("/");
                    String host = pathArr[pathArr.length - 1];
                    String[] ipPort = host.split(":");
                    NodeInfo nodeInfo = new NodeInfo(ipPort[0], Integer.parseInt(ipPort[1]));
                    boolean online = true;
                    if (data != null && data.length > 1) {
                        String msg = new String(data, Charset.forName("UTF-8"));
                        Gson gson = new Gson();
                        ServerNodeStatus status = gson.fromJson(msg, ServerNodeStatus.class);
                        if (status.getStatus() == -1) {
                            online = false;
                        }
                    }
                    if (eventType == Watcher.Event.EventType.NodeCreated) {
                        LOGGER.info("Get server node {} online={}", nodeInfo.getPath(), online);
                        if (online) {
                            Channel ch = initChannel(nodeInfo.getIp(), nodeInfo.getPort());
                            InetSocketAddress sock = (InetSocketAddress) ch.localAddress();
                            zkClient.create(clientPath + sock.getAddress() + ":" + sock.getPort(), new byte[1], true);
                            route.addServerNode(nodeInfo, ch);
                            LOGGER.info("Connection server {} success.", nodeInfo.getPath());
                        }
                    } else if (eventType == Watcher.Event.EventType.NodeDeleted) {
                        Channel ch = route.removeServerNode(nodeInfo.getPath());
                        LOGGER.info("Delete server node :{}", nodeInfo.getPath());
                        if (ch != null) {
                            nettyClient.close(ch);
                            InetSocketAddress sock = (InetSocketAddress) ch.localAddress();
                            zkClient.delete(clientPath + sock.getAddress() + ":" + sock.getPort());
                            LOGGER.info("Close server {} connection.", nodeInfo.getPath());
                        }
                    } else {//数据变化
                        if (online) {
                            LOGGER.info("Server node online {}", nodeInfo.getPath());
                            Channel ch = route.getServer(nodeInfo.getPath());
                            if (ch == null) {
                                ch = initChannel(nodeInfo.getIp(), nodeInfo.getPort());
                                InetSocketAddress sock = (InetSocketAddress) ch.localAddress();
                                zkClient.create(clientPath + sock.getAddress() + ":" + sock.getPort(), new byte[1], true);
                                route.addServerNode(nodeInfo, ch);
                                LOGGER.info("Connection server {} success.", nodeInfo.getPath());
                            }
                        } else {
                            LOGGER.info("Server node offline {}", nodeInfo.getPath());
                            Channel ch = route.removeServerNode(nodeInfo.getPath());
                            if (ch != null) {
                                nettyClient.close(ch);
                                InetSocketAddress sock = (InetSocketAddress) ch.localAddress();
                                zkClient.delete(clientPath + sock.getAddress() + ":" + sock.getPort());
                                LOGGER.info("Close server {} connection.", nodeInfo.getPath());
                            }
                        }
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
    public Channel getChannel(String serviceName) throws InterruptedException {
        Channel ch = this.route.getServer();
        if (ch == null) {
            throw new ScudExecption("There is no service available, service name : " + serviceName);
        }
        return ch;
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
        RpcFuture<T> rpcFuture = new ResponseFuture<>(config.getTimeout());
        MessageManager.setSeq(seq, rpcFuture);
        this.invoker.invoke(this.getChannel(serviceName), protocol, seq);
        T result;
        try {
            result = rpcFuture.get(config.getTimeout(), TimeUnit.MILLISECONDS);
        } finally {
            MessageManager.remove(protocol.getSequence());
        }
        return result;
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
        RpcFuture<T> rpcFuture = new ResponseFuture<>(config.getTimeout());
        MessageManager.setSeq(seq, rpcFuture);
        this.invoker.invoke(this.getChannel(serviceName), protocol, seq);
        return rpcFuture;
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
        MessageManager.setSeq(seq, rpcFuture);
        this.invoker.invoke(this.getChannel(serviceName), protocol, seq);
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

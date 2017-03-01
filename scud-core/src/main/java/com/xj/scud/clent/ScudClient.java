package com.xj.scud.clent;

import com.xj.scud.core.ProtocolProcesser;
import com.xj.scud.network.proxy.RpcClientProxy;

/**
 * Author: xiajun
 * Date: 2017/01/03 18:01
 */
public class ScudClient<T> {
    private T client;
    private ClientConfig conf;
    private ProtocolProcesser processer;
    private ClientManager manager;

    public ScudClient(ClientConfig conf) {
        this.conf = conf;
    }

    public void init() {
        if (manager == null) {
            synchronized (ScudClient.class) {
                if (manager == null) {
                    this.processer = new ProtocolProcesser(this.conf);
                    this.manager = new ClientManager(processer, this.conf);
                    this.manager.initCluster();
                }
            }
        }
    }

    /**
     * 阻塞的客户端
     *
     * @return T
     */
    public T getClient() {
        if (manager == null) {
            throw new IllegalStateException("Uninitialized ScudClient class.");
        }
        synchronized (ScudClient.class) {
            if (client == null) {
                RpcClientProxy proxy = new RpcClientProxy(manager);
                this.client = (T) proxy.getProxy(this.conf.getServiceClass());
            }
        }
        return this.client;
    }

    /**
     * 异步客户端
     * 暂未实现
     *
     * @return
     */
    public T getAsyncClient() {
        return null;
    }
}

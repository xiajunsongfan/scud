package com.xj.scud.client;

import com.xj.scud.core.ProtocolProcesser;
import com.xj.scud.core.network.proxy.RpcClientProxy;

/**
 * Author: xiajun
 * Date: 2017/01/03 18:01
 */
public class ScudClient<T> {
    private volatile T client;
    private ClientConfig conf;
    private volatile ClientManager manager;

    public ScudClient(ClientConfig conf) {
        this.conf = conf;
    }

    public void init() {
        if (manager == null) {
            synchronized (ScudClient.class) {
                if (manager == null) {
                    this.manager = new ClientManager(new ProtocolProcesser(this.conf), this.conf);
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
                this.client = (T) proxy.getProxy(this.conf.getInterfaze());
            }
        }
        return this.client;
    }
}

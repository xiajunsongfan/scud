package com.xj.scud.server;

import com.xj.scud.network.netty.NettyServer;
import com.xj.scud.core.ServiceMapper;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/04 09:40
 */
public class ScudServer {
    private ServerConfig config;

    public ScudServer(ServerConfig config) {
        this.config = config;
    }

    public void start() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, config.getCorePoolSize(), 30, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new DefaultThreadFactory("scud-server-work",true), new ThreadPoolExecutor.CallerRunsPolicy());
        ServiceMapper.init(this.config.getServiceClass());
        ServerManager manager = new ServerManager(config, executor);
        NettyServer.start(this.config, manager);
    }
}

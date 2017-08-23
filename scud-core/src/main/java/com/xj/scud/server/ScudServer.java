package com.xj.scud.server;

import com.xj.scud.commons.Config;
import com.xj.scud.core.ServiceMapper;
import com.xj.scud.core.network.netty.NettyServer;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/04 09:40
 */
public class ScudServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(ScudServer.class);
    private volatile static boolean isRun = false;//暂时不允许启动多个server
    private ServerConfig config;
    private Provider[] providers;

    public ScudServer(Provider... providers) {
        this.config = Config.buildServerConfig();
        this.providers = providers;
    }

    public void start() {
        if (!isRun) {
            isRun = true;
            ThreadPoolExecutor executor = new ThreadPoolExecutor(2, config.getCorePoolSize(), 30, TimeUnit.SECONDS, new SynchronousQueue<>(), new DefaultThreadFactory("scud-server-work", true), new ThreadPoolExecutor.CallerRunsPolicy());
            ServiceMapper.init(this.providers);
            ServerManager manager = new ServerManager(config, executor);
            NettyServer.start(this.config, manager);
            LOGGER.info("Scud server start config info:{}",config.toString());
        } else {
            throw new RuntimeException("Scud server cannot start multiple times.");
        }
    }
}

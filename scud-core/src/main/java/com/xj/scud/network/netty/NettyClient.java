package com.xj.scud.network.netty;

import com.xj.scud.clent.ClientConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/02 13:54
 */
public class NettyClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    public static Channel start(ClientConfig config) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, getThreadSize(config.getWorkThreadSize()), 30, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new DefaultThreadFactory("scud-client-work",true), new ThreadPoolExecutor.CallerRunsPolicy());

        EventLoopGroup boss = new NioEventLoopGroup(2, new DefaultThreadFactory("netty-client-boss"));
        Bootstrap boot = new Bootstrap();
        boot.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 65536));
        boot.group(boss).channel(NioSocketChannel.class).handler(new NettyClientInitializer(executor));

        return boot.connect(config.getIp(), config.getPort()).channel();
    }

    private static int getThreadSize(int size) {
        if (size < 1) {
            size = Runtime.getRuntime().availableProcessors();
        }
        return size;
    }
}

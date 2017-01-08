package com.xj.scud.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/02 14:32
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private ThreadPoolExecutor executor;

    public NettyClientInitializer(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("decoder", new NettyMessageDecoder());
        pipeline.addLast("encoder", new NettyMessageEncoder());
        pipeline.addLast("handler", new NettyClientHandler(this.executor));
    }
}

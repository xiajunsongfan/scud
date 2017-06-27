package com.xj.scud.core.network.netty;

import com.xj.scud.server.ServerManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Author: xiajun
 * Date: 2017/01/02 14:22
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServerManager manager;

    public NettyServerInitializer(ServerManager manager) {
        this.manager = manager;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 字符串解码 和 编码
        pipeline.addLast("decoder", new NettyMessageDecoder());
        pipeline.addLast("encoder", new NettyMessageEncoder());
        // 自己的逻辑Handler
        pipeline.addLast("handler", new NettyServerHandler(manager));
    }
}

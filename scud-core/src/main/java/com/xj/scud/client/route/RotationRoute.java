package com.xj.scud.client.route;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: xiajun
 * Date: 2017/01/09 10:49
 */
public class RotationRoute extends RpcRoute {
    private AtomicInteger index = new AtomicInteger(0);


    @Override
    public boolean addServerNode(NodeInfo node, Channel channel) {
        String key = node.getPath();
        serverNodes.put(key, channel);
        nodes.add(channel);
        return true;
    }

    @Override
    public Channel removeServerNode(String key) {
        Channel channel;
        if ((channel = serverNodes.remove(key)) != null) {
            nodes.remove(channel);
        }
        return channel;
    }

    @Override
    public Channel getServer() {
        int i = Math.abs(index.incrementAndGet());
        if (serverNodes.isEmpty()) {
            return null;
        }
        int key = i % serverNodes.size();
        return nodes.get(key);
    }

    @Override
    public int size() {
        return serverNodes.size();
    }
}
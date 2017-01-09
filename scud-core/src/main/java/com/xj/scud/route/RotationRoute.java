package com.xj.scud.route;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/09 10:49
 */
public class RotationRoute extends RpcRoute {
    private AtomicInteger index = new AtomicInteger(0);
    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public boolean addServerNode(NodeInfo node, Channel channel) {
        String key = node.getPath();
        lock.writeLock().lock();
        try {
            serverNodes.put(key, channel);
            nodes.add(channel);
        } finally {
            lock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public boolean removeServerNode(String key) {
        boolean res = false;
        lock.writeLock().lock();
        try {
            Channel channel;
            if ((channel = serverNodes.remove(key)) != null) {
                nodes.remove(channel);
                res = true;
            }
        } finally {
            lock.writeLock().unlock();
        }
        return res;
    }

    @Override
    public Channel getServer() {
        int i = Math.abs(index.incrementAndGet());
        lock.readLock().lock();
        try {
            if (serverNodes.isEmpty()) {
                return null;
            }
            int key = i % serverNodes.size();
            return nodes.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return serverNodes.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
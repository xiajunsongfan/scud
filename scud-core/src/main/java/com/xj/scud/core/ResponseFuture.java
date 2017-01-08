package com.xj.scud.core;

import java.util.concurrent.*;

/**
 * Author: xiajun
 * Date: 2017/01/06 19:24
 * 结果封装Future
 */
public class ResponseFuture<T> implements Future<T> {
    private Semaphore lock = new Semaphore(0);
    private T response;
    private boolean done = false;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        if (!isDone()) {
            lock.acquire();
        }
        return this.response;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!isDone()) {
            boolean done = lock.tryAcquire(timeout, unit);
            if (!done) {
                throw new TimeoutException("Waiting response timeout!");
            }
        }
        return this.response;
    }

    /**
     * 设置结果
     * @param response 服务返回结果
     */
    public void responseReceived(T response) {
        this.response = response;
        this.done = true;
        this.lock.release();
    }
}

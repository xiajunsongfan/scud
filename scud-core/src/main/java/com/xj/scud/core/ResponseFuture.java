package com.xj.scud.core;

import com.xj.scud.core.exception.ScudExecption;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Author: xiajun
 * Date: 2017/01/06 19:24
 * 结果封装Future
 */
public class ResponseFuture<T> extends RpcFuture<T> {
    private Semaphore lock;
    private T response;
    private volatile boolean done = false;
    private volatile Throwable throwable;

    public ResponseFuture(int invokerTimeout) {
        super(System.currentTimeMillis(), invokerTimeout);
    }

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
        return this.get(0, TimeUnit.SECONDS);
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
        if (!isDone()) {
            if (timeout > 0) {
                boolean done = this.getCondition().tryAcquire(timeout, unit);
                if (!done) {
                    throw new ScudExecption("Waiting response timeout! timeout=" + timeout + "ms");
                }
            } else {
                this.getCondition().acquire();
            }
        }
        if (throwable != null) {
            throw new ScudExecption("Service provider exception.", throwable);
        }
        return this.response;
    }

    private Semaphore getCondition() {
        if (lock == null) {
            synchronized (ResponseFuture.class) {
                if (lock == null) {
                    lock = new Semaphore(0);
                }
            }
        }
        return lock;
    }

    /**
     * 设置结果
     *
     * @param result 服务返回结果
     */
    public void responseReceived(RpcResult result) {
        genResult(result);
        this.done = true;
        this.getCondition().release();
        if (throwable != null) {
            this.completeExceptionally(throwable);
        }
        this.complete(response);
    }

    private void genResult(RpcResult result) {
        if (result != null) {
            Throwable exception = result.getException();
            if (exception != null) {
                throwable = exception;
            }
            this.response = (T) result.getValue();
        } else {
            this.response = null;
        }
    }
}

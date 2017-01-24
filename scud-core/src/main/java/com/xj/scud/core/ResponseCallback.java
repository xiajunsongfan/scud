package com.xj.scud.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Author: xiajun
 * Date: 2017/01/20 10:50
 */
public class ResponseCallback<T> implements RpcFuture<RpcResult> {
    private RpcCallback callback;
    private boolean done = false;

    public ResponseCallback(RpcCallback callback) {
        this.callback = callback;
    }

    public ResponseCallback() {
    }

    @Override
    public void responseReceived(RpcResult response) {
        this.done = true;
        Throwable exception = response.getException();
        if (exception != null) {
            callback.fail(exception);
        } else {
            callback.success(response.getValue());
        }
    }

    @Override
    public void coypFuture(RpcFuture<RpcResult> future) {

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
        return done;
    }

    @Override
    public RpcResult get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public RpcResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}

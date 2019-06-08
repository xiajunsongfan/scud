package com.xj.scud.core;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author: xiajun
 * Date: 2017/01/20 14:28
 */
public class RpcContext {
    private boolean isFutureInvoke = false;
    private boolean isCallbackInvoke = false;
    private RpcCallback rpcCallback;
    private RpcFuture future;
    private static ThreadPoolExecutor serverExecutor;

    private static final ThreadLocal<RpcContext> LOCAL = ThreadLocal.withInitial(() -> new RpcContext());

    public static RpcContext getContext() {
        return LOCAL.get();
    }

    public static void invokeWithCallback(AsyncPrepare prepare, RpcCallback rpcCallback) {
        if (null == prepare) {
            throw new NullPointerException("prepare");
        }
        if (null == rpcCallback) {
            throw new NullPointerException("rpcCallback");
        }
        RpcContext rpcContext = LOCAL.get();
        try {
            rpcContext.isCallbackInvoke = true;
            rpcContext.rpcCallback = rpcCallback;
            prepare.prepare();
        } finally {
            rpcContext.clearAsyncContext();
        }
    }

    public static <T> Future<T> invokeWithFuture(AsyncPrepare prepare) {
        if (null == prepare) {
            throw new NullPointerException("prepare");
        }
        RpcContext rpcContext = LOCAL.get();
        try {
            rpcContext.isFutureInvoke = true;
            prepare.prepare();
            RpcFuture rpcFuture = rpcContext.future;
            return  rpcFuture;
        } finally {
            rpcContext.clearAsyncContext();
        }
    }

    private void clearAsyncContext() {
        this.isCallbackInvoke = false;
        this.isFutureInvoke = false;
        this.rpcCallback = null;
        this.future = null;
    }

    public boolean isFutureInvoke() {
        return isFutureInvoke;
    }

    public boolean isCallbackInvoke() {
        return isCallbackInvoke;
    }

    public RpcCallback getRpcCallback() {
        return rpcCallback;
    }

    public void setFuture(RpcFuture future) {
        this.future = future;
    }

    public static ThreadPoolExecutor getServerExecutor() {
        return serverExecutor;
    }

    public static void setServerExecutor(ThreadPoolExecutor serverExecutor) {
        RpcContext.serverExecutor = serverExecutor;
    }
}

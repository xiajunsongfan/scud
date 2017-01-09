package com.xj.scud.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 18:48
 */
public class MessageManager {
    private static Map<Integer, ResponseFuture> msgManager = new ConcurrentHashMap<>(128);


    /**
     * 设置等待队列
     *
     * @param seq 等待序号
     */
    public static ResponseFuture setSeq(int seq) {
        ResponseFuture<RpcResult> future = new ResponseFuture<>();
        msgManager.put(seq, future);
        return future;
    }

    /**
     * 服务端返回结果后，释放客户端
     *
     * @param seq    包序号
     * @param result 服务端返回的结果
     */
    public static void release(int seq, RpcResult result) {
        ResponseFuture future = msgManager.remove(seq);
        if (future != null) {
            future.responseReceived(result);
        }
    }

    /**
     * 删除队列
     * @param seq 包序号
     */
    public static void remove(int seq){
        msgManager.remove(seq);
    }
}

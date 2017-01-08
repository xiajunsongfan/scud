package com.xj.scud.server;

import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.ProtocolProcesser;
import com.xj.scud.core.ServiceMapper;
import com.xj.scud.core.RpcInvocation;
import com.xj.scud.core.RpcResult;
import com.xj.scud.network.SerializableHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/04 09:40
 * 服务处理类
 */
public class ServerManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerManager.class);
    private ProtocolProcesser protocolProcesser;
    private ThreadPoolExecutor executor;
    private ServerConfig config;

    /**
     * 构造函数
     *
     * @param config   配置
     * @param executor 执行线程池
     */
    public ServerManager(ServerConfig config, ThreadPoolExecutor executor) {
        this.executor = executor;
        this.config = config;
        protocolProcesser = new ProtocolProcesser(null);
    }

    /**
     * 执行服务接口
     *
     * @param protocol 网络协议对象
     * @param ctx      channel对象
     */
    public void invoke(final NetworkProtocol protocol, final ChannelHandlerContext ctx) {
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                RpcInvocation invocation = SerializableHandler.requestDecode(protocol);
                long reqTime = invocation.getRequestTime();
                int timeout = invocation.getRequestTimeout();
                if (System.currentTimeMillis() - reqTime < timeout) {//超时的任务就不用执行了
                    String methodName = ProtocolProcesser.buildMethodName(invocation.getMethod(), invocation.getArgTypes());
                    Object res = null;
                    Throwable throwable = null;
                    try {
                        res = invoke0(methodName, invocation.getArgs());
                    } catch (Exception e) {
                        throwable = e;
                        LOGGER.error("Invoke exception.", e);
                    }
                    try {
                        RpcResult result = buildRpcResult(200, throwable, res);
                        NetworkProtocol responseProtocol = protocolProcesser.buildResponseProtocol(protocol, result);
                        ctx.writeAndFlush(responseProtocol);
                    } catch (Exception e) {
                        LOGGER.error("Server invoke fail.", e);
                    }
                }
            }
        });
    }

    /**
     * 执行方法
     *
     * @param method 方法对象
     * @param args   方法参数
     * @return Object
     * @throws InvocationTargetException e
     * @throws IllegalAccessException    e
     */
    private Object invoke0(String method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Method m = ServiceMapper.getMethod(method);
        return m.invoke(this.config.getService(), args);
    }


    private RpcResult buildRpcResult(int status, Throwable throwable, Object res) {
        RpcResult rpcResult = new RpcResult();
        rpcResult.setException(throwable);
        rpcResult.setStatus(status);
        rpcResult.setValue(res);
        return rpcResult;
    }
}

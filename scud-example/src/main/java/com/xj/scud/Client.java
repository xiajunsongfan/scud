package com.xj.scud;

import com.xj.scud.client.ClientConfig;
import com.xj.scud.client.ScudClientFactory;
import com.xj.scud.client.route.RouteEnum;
import com.xj.scud.core.AsyncPrepare;
import com.xj.scud.core.RpcCallback;
import com.xj.scud.core.RpcContext;
import com.xj.scud.core.network.SerializableEnum;
import com.xj.scud.idl.Test;
import com.xj.scud.idl.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Author: xiajun
 * Date: 2017/01/04 12:05
 */
public class Client {
    public static void main(String[] args) throws Exception {
        final ClientConfig conf = new ClientConfig();
        //conf.setRoute(RouteEnum.RANDOM).setTimeout(2000).setInterfaze(Test.class).setVersion("1.0.1")
        //        .setType(SerializableEnum.PROTOBUF).setUseZk(true).setZkHost("127.0.0.1:2181");

        conf.setRoute(RouteEnum.RANDOM).setTimeout(2000).setInterfaze(Test.class).setVersion("1.0.1")
                .setType(SerializableEnum.PROTOBUF).setUseZk(false).setHost("127.0.0.1:6157");

        final Test t = ScudClientFactory.getServiceConsumer(conf);


        //* 同步阻塞模式 *
        Long stime = System.currentTimeMillis();
        //CompletableFuture<User> future = t.testAsync("CompletableFuture-test");
        //future.thenAccept(user -> System.out.println("CompletableFuture: " + user + " " + (System.currentTimeMillis() - stime)));

        //* 异步Future模式 *
        Future<User> f = RpcContext.invokeWithFuture(() -> t.test("async-test"));
        System.out.println("Async Future:" + f.get() + " " + (System.currentTimeMillis() - stime));

        //* 异步Callback模式 *
        RpcContext.invokeWithCallback(() -> t.test("test-callback"), new RpcCallback() {
            @Override
            public void success(Object value) {
                System.out.println("callback: " + value + " " + (System.currentTimeMillis() - stime));
            }

            @Override
            public void fail(Throwable error) {
                error.printStackTrace();
            }
        });
        User u = t.test("block-test");
        System.out.println("Block: " + u.toString() + " cost: " + (System.currentTimeMillis() - stime) + "ms");

    }
}

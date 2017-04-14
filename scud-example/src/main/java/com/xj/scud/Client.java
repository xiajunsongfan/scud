package com.xj.scud;

import com.xj.scud.clent.ClientConfig;
import com.xj.scud.clent.ScudClientFactory;
import com.xj.scud.core.AsyncPrepare;
import com.xj.scud.core.RpcCallback;
import com.xj.scud.core.RpcContext;
import com.xj.scud.idl.Test;
import com.xj.scud.idl.User;
import com.xj.scud.network.SerializableEnum;
import com.xj.scud.route.RouteEnum;

import java.util.concurrent.Future;

/**
 * Author: xiajun
 * Date: 2017/01/04 12:05
 */
public class Client {
    public static void main(String[] args) throws Exception {
        final ClientConfig<Test> conf = new ClientConfig();
        conf.setHost("127.0.0.1:7890").setRoute(RouteEnum.RANDOM).setTimeout(2000).setServiceClass(Test.class).setWorkThreadSize(1).setType(SerializableEnum.PROTOBUF);
        final Test t = ScudClientFactory.getServiceConsumer(conf);

        /** 同步阻塞模式 **/
        Long stime = System.currentTimeMillis();
        String u = t.test2();
        System.out.println(u.toString() + " cost: " + (System.currentTimeMillis() - stime) + "ms");

        /** 异步Future模式 **/
        Future<User> f = RpcContext.invokeWithFuture(new AsyncPrepare() {
            @Override
            public void prepare() {
                t.test("test");
            }
        });
        System.out.println(f.get());

        /** 异步Callback模式 **/
        RpcContext.invokeWithCallback(new AsyncPrepare() {
            @Override
            public void prepare() {
                t.test("test");
            }
        }, new RpcCallback() {
            @Override
            public void success(Object value) {
                System.out.println("callback: " + value);
            }

            @Override
            public void fail(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}
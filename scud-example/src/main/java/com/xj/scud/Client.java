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

import java.util.concurrent.Future;

/**
 * Author: xiajun
 * Date: 2017/01/04 12:05
 */
public class Client {
    public static void main(String[] args) throws Exception {
        final ClientConfig conf = new ClientConfig();
        conf.setRoute(RouteEnum.RANDOM).setTimeout(2000).setInterfaze(Test.class).setVersion("1.0.1")
                .setType(SerializableEnum.PROTOBUF).setUseZk(true).setZkHost("127.0.0.1:2181");
        final Test t = ScudClientFactory.getServiceConsumer(conf);

   /*     * 同步阻塞模式 *
        Long stime = System.currentTimeMillis();
        String u = t.test2();
        System.out.println(u.toString() + " cost: " + (System.currentTimeMillis() - stime) + "ms");
*/
        //* 异步Future模式 *
        Future<User> f = RpcContext.invokeWithFuture(new AsyncPrepare() {
            @Override
            public void prepare() {
                t.test("12",12);
            }
        });
        System.out.println("-----------------------------------");
        f = RpcContext.invokeWithFuture(new AsyncPrepare() {
            @Override
            public void prepare() {
                t.test("13",13);
            }
        });
        System.out.println("-----------------------------------");
        //* 异步Callback模式 *
        /*RpcContext.invokeWithCallback(new AsyncPrepare() {
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
        });*/
    }
}

package com.xj.scud;

import com.xj.scud.client.ClientConfig;
import com.xj.scud.client.ScudClientFactory;
import com.xj.scud.client.route.RouteEnum;
import com.xj.scud.core.network.SerializableEnum;
import com.xj.scud.idl.Test;
import com.xj.scud.idl.User;

/**
 * Author: xiajun
 * Date: 2017/01/04 12:05
 */
public class Client {
    public static void main(String[] args) throws Exception {
        final ClientConfig conf = new ClientConfig();
        conf.setRoute(RouteEnum.RANDOM).setTimeout(2000).setInterfaze(Test.class).setVersion("1.0.1").setWorkThreadSize(1)
                .setType(SerializableEnum.PROTOBUF).setHost("127.0.0.1:6157").setUseZk(false).setZkHost("127.0.0.1:2181");
        final Test t = ScudClientFactory.getServiceConsumer(conf);
        for (int i = 0; i < 30000; i++) {
            try {
                User u = t.test("" + i);
                System.out.println(u.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(200);
        }


   /*     * 同步阻塞模式 *
        Long stime = System.currentTimeMillis();
        String u = t.test2();
        System.out.println(u.toString() + " cost: " + (System.currentTimeMillis() - stime) + "ms");

        * 异步Future模式 *
        Future<User> f = RpcContext.invokeWithFuture(new AsyncPrepare() {
            @Override
            public void prepare() {
                t.test("test");
            }
        });
        System.out.println(f.get());

        * 异步Callback模式 *
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
        });*/
    }
}

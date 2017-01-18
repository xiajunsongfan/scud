package com.xj.scud;

import com.xj.scud.clent.ClientConfig;
import com.xj.scud.clent.ScudClientFactory;
import com.xj.scud.idl.Test;
import com.xj.scud.idl.User;
import com.xj.scud.network.SerializableEnum;
import com.xj.scud.route.RouteEnum;

/**
 * Author: xiajun
 * Date: 2017/01/04 12:05
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        ClientConfig<Test> conf = new ClientConfig();
        conf.setHost("127.0.0.1:7890").setRoute(RouteEnum.RANDOM).setTimeout(2000).setServerClass(Test.class).setWorkThreadSize(1).setType(SerializableEnum.PROTOBUF);
        Test t = ScudClientFactory.getServiceConsumer(conf);

        long st = System.currentTimeMillis();
        for (int i = 0; i < 6; i++) {
            String u = t.test();
            System.out.println(u.toString());
        }
        System.out.println((System.currentTimeMillis() - st) + "ms ");
        Thread.sleep(10000);
        String u = t.test();
        System.out.println(u);
    }
}

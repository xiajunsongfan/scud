package com.xj.scud;

import com.xj.scud.clent.ClientConfig;
import com.xj.scud.clent.ScudClientFactory;
import com.xj.scud.idl.Test;
import com.xj.scud.idl.User;
import com.xj.scud.network.SerializableEnum;

/**
 * Author: xiajun
 * Date: 2017/01/04 12:05
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        ClientConfig<Test> conf = new ClientConfig();
        conf.setIp("127.0.0.1").setPort(7890).setTimeout(2000).setServerClass(Test.class).setWorkThreadSize(1).setType(SerializableEnum.PROTOBUF);
        Test t = ScudClientFactory.getServiceConsumer(conf);

        long st = System.currentTimeMillis();
        String u = t.test();
        System.out.println(u.toString());
        User user = t.test("test2");
        System.out.println(user.toString());
        System.out.println((System.currentTimeMillis() - st) + "ms ");
    }
}

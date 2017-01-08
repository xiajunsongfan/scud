package com.xj.scud;

import com.xj.scud.idl.Test;
import com.xj.scud.idl.TestImpl;
import com.xj.scud.server.ScudServer;
import com.xj.scud.server.ServerConfig;

/**
 * Author: xiajun
 * Date: 2017/01/04 12:08
 */
public class Server {
    public static void main(String[] args) {
        ServerConfig conf = new ServerConfig();
        conf.setPort(7890).setServiceClass(Test.class).setService(new TestImpl()).setCorePoolSize(12);
        ScudServer server = new ScudServer(conf);
        server.start();
    }
}

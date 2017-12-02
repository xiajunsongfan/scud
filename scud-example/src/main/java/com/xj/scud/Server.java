package com.xj.scud;

import com.xj.scud.idl.Test;
import com.xj.scud.idl.TestImpl;
import com.xj.scud.server.Provider;
import com.xj.scud.server.ScudServer;

/**
 * Author: xiajun
 * Date: 2017/01/04 12:08
 */
public class Server {
    public static void main(String[] args) {
        Provider<Test> provider = new Provider<>(Test.class, new TestImpl(), "1.0.1");
        ScudServer server = new ScudServer(provider);
        server.start();
    }
}

package com.xj.scud.btrace;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;
@BTrace
public class ServerBtrace {
    @OnMethod(clazz = "com.xj.scud.idl.TestImpl", method = "test2", location = @Location(Kind.RETURN))
    public static void test(@Return String value, @Duration long duration) {
        BTraceUtils.print("return: ");
        BTraceUtils.print(" cost: ");
        BTraceUtils.println(duration / 100000);
    }
}

package com.xj.scud;

import com.xj.scud.annotation.Client;
import com.xj.scud.idl.Test;
import com.xj.scud.idl.User;
import org.springframework.stereotype.Component;

/**
 * @author: xiajun
 * @date: 2019/5/24 下午9:26
 * @since 1.0.0
 */
@Component
public class SpringAnnotationClient {
    @Client(version = "1.0.1", host = "127.0.0.1:6157", lazy = true)
    private Test clent;

    private void test() {
        User u = clent.test("123");
        System.out.println(u + " test");
    }
}

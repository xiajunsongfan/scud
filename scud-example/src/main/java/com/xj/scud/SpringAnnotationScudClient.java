package com.xj.scud;

import com.xj.scud.annotation.Client;
import com.xj.scud.idl.Test;
import org.springframework.stereotype.Component;

/**
 * @author: xiajun
 * @date: 2019/5/25 18:42
 * @since 1.0.0
 */
@Component
public class SpringAnnotationScudClient {
    @Client(version = "1.0.1", host = "127.0.0.1:6157", lazy = true)
    private Test clent;

    public Test getTest(){
        return clent;
    }
}

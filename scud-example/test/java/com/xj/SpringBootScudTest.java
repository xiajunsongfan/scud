package com.xj;

import com.xj.scud.SpringAnnotationScudClient;
import com.xj.scud.SpringAnnotationScudServer;
import com.xj.scud.idl.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author: xiajun
 * @date: 2019/5/25 18:08
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringAnnotationScudServer.class)
@Component
public class SpringBootScudTest {
    @Resource
    private SpringAnnotationScudClient scudClient;

    @Test
    public void test() {
        User u = scudClient.getTest().test("annotation");
        Assert.notNull(u,"未返回正确的对象");
        Assert.isTrue("annotation".equals(u.getName()),"结果错误");
        System.out.println(u);
    }
}

import com.xj.scud.spring.bean.ServerConfigBean;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 14:23
 */
public class ScudTest {
    private ClassPathXmlApplicationContext context;

    @Before
    public void init() {
        context = new ClassPathXmlApplicationContext("classpath:scud.xml");
    }

    @Test
    public void test() {
        ServerConfigBean bean = (ServerConfigBean) context.getBean("serverConfig");

        System.out.println("------------>" + bean.getPort());
    }
}

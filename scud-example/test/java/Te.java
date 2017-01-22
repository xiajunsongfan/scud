import com.xj.scud.idl.User;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/20 15:29
 */
public class Te {
    public static void main(String[] args) throws InterruptedException {
        long s = System.currentTimeMillis();
        System.out.println(Te.test());
        System.out.println(System.currentTimeMillis() - s);
    }

    public static User test() throws InterruptedException {
        User u = new User();
        try {
            u.setName("123");
            return u;
        } finally {
            Thread.sleep(2000);
            System.out.println("-----------------");
            u = null;
        }

    }
}

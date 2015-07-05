package reflect;

import org.junit.Test;

import util.ReflectUtil;

public class TestUtil {
    @Test
    public void testReflect() {
        User1 user = new User1();
        long start = System.currentTimeMillis();
        
        ReflectUtil.setFieldValue(user, "test0", "haha");
        Object value = ReflectUtil.getFieldValue(user, "test0");
        
        System.out.println(value);
        
        long end = System.currentTimeMillis();
        
        System.out.println(end - start);
    }
}

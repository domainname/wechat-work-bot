package com.github.domainname.util;

 import com.github.domainname.util.ExceptionUtils;
 import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author jeff
 * @date 2019-10-27
 */
@RunWith(SpringRunner.class)
public class ExceptionUtilsTests {

    @Test
    public void testExceptionWithoutCause() {
        IllegalArgumentException ex = new IllegalArgumentException("illegal arg");
        assertEquals("java.lang.IllegalArgumentException: illegal arg",
                ExceptionUtils.toString(ex));
    }

    @Test
    public void testExceptionWithCause() {
        RuntimeException cause = new RuntimeException("cause");
        IllegalArgumentException ex = new IllegalArgumentException("illegal arg", cause);
        assertEquals("java.lang.IllegalArgumentException: illegal arg；" +
                        "嵌套的异常为 java.lang.RuntimeException: cause",
                ExceptionUtils.toString(ex));
    }

    @Test
    public void testExceptionWithCauses() {
        Exception cause0 = new RuntimeException("cause0");
        RuntimeException cause1 = new RuntimeException("cause1", cause0);
        IllegalArgumentException ex = new IllegalArgumentException("illegal arg", cause1);
        assertEquals("java.lang.IllegalArgumentException: illegal arg；" +
                        "嵌套的异常为 java.lang.RuntimeException: cause1；" +
                        "嵌套的异常为 java.lang.RuntimeException: cause0",
                ExceptionUtils.toString(ex));
    }
}

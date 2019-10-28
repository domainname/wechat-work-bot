package com.github.domainname.util;

import org.slf4j.Logger;

/**
 * @author jeff
 * @date 2019-10-27
 */
public abstract class ExceptionUtils {

    /**
     * 将异常转成易于阅读的字符串，支持多层嵌套异常。
     *
     * @param ex
     * @return
     */
    public static String toString(Throwable ex) {
        String result = ex.toString();

        if (ex.getCause() != null) {
            result += "；嵌套的异常为 " + toString(ex.getCause());
        }

        return result;
    }

    /**
     * 将异常记录到日志里。如果 logger 打开了 debug，还将记录 stack trace。
     *
     * @param log
     * @param ex
     */
    public static void logException(Logger log, Exception ex) {
        log.info("发生异常 {}", toString(ex));

        if (log.isDebugEnabled()) {
            ex.printStackTrace();
        }
    }
}

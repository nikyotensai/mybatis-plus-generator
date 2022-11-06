package com.github.nikyotensai.config.util;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * @author nikyotensai
 * @since 2022/11/6
 */
public class LogUtil {

    private static final Log log = new SystemStreamLog();

    public static void info(String msg) {
        log.info(msg);
    }


    public static void error(String msg) {
        log.error(msg);
    }


}

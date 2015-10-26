package com.pureblue.quant.util;

import java.io.File;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LoggerUtils {
    public static final String LOG_LEVEL_PROPERTY_KEY = "log.level";
    public static final String path = "D:/";

    public static Logger getLogger(String path) {
        // 生成新的Logger
        // 如果已經有了一個Logger實例返回現有的
        Logger logger = Logger.getLogger(LoggerUtils.class.getName());
        // 清空Appender。特別是不想使用現存實例時一定要初期化
        logger.removeAllAppenders();
        // 設定Logger級別。
        Level level = Level.INFO;
        String levelStr = System.getProperty(LOG_LEVEL_PROPERTY_KEY);
        if (!levelStr.isEmpty())
            level = convertTolog4jLevel(levelStr);
        logger.setLevel(level);
        // 設定是否繼承父Logger。
        // 默認為true。繼承root輸出。
        // 設定false後將不輸出root。
        logger.setAdditivity(true);
        // 生成新的Appender
        FileAppender appender = new FileAppender();
        PatternLayout layout = new PatternLayout();
        // log的输出形式
        String conversionPattern = "[%d{yyyy-MM-dd HH:mm:ss}] %p [%t] %m%n";
        layout.setConversionPattern(conversionPattern);
        appender.setLayout(layout);
        // log输出路径
        // 这里使用了环境变量[catalina.home]，只有在tomcat环境下才可以取到
        appender.setFile(path + File.separator + "log.log");
        // log的文字码
        appender.setEncoding("UTF-8");
        // true:在已存在log文件后面追加 false:新log覆盖以前的log
        appender.setAppend(true);
        // 适用当前配置
        appender.activateOptions();
        // 将新的Appender加到Logger中
        logger.addAppender(appender);
        return logger;
    }

    private static Level convertTolog4jLevel(String levelStr) {
        switch (levelStr) {
        case "debug":
            return Level.DEBUG;
        case "info":
            return Level.INFO;
        case "warn":
            return Level.WARN;
        case "error":
            return Level.ERROR;
        case "fatal":
            return Level.FATAL;
        default:
            return Level.WARN;
        }
    }
}
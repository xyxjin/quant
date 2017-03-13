package com.pureblue.quant.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class StockDatebaseFactory {
    private static Logger logger;

    public StockDatebaseFactory() {
    }

    public static Connection getInstance(String dataBase){
//        StockDatebaseFactory.logger = LoggerUtils.getLogger(LoggerUtils.path);
        StockDatebaseFactory.logger = Logger.getLogger(StockDatebaseFactory.class);
        logger.debug("StockDatebaseFactory::getInstance: get SQL datebase " + dataBase + " entry.");
        Connection connection = null;
        String url = "jdbc:mysql://127.0.0.1:3306/" + dataBase;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // System.out.println("成功加载MySQL驱动程序");
            connection = DriverManager.getConnection(url, "root", null);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.fatal("StockDatebaseFactory::getInstance: get connect to SQL datebase error with " + e.toString());
        } catch (Exception e) {
            logger.fatal("StockDatebaseFactory::getInstance: SQL datebase driver error with " + e.toString());
        }
        logger.debug("StockDatebaseFactory::getInstance: get SQL datebase " + dataBase + " exit.");
        return connection;
    }
}

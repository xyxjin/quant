package com.pureblue.quant.quantAPI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.pureblue.quant.TencentAPI.TencentRealTimeAdapterComponent;
import com.pureblue.quant.dao.CapitalPointDao;
import com.pureblue.quant.dao.ICapitalPoint;
import com.pureblue.quant.dao.StockDatebaseFactory;

import com.pureblue.quant.ConnectionPool.ConnectionManager;
import com.pureblue.quant.ConnectionPool.DBPropertyBean;
import com.pureblue.quant.ConnectionPool.IConnectionPool;
import com.pureblue.quant.ConnectionPool.PropertiesManager;

public class TencentRealTimeThread implements Runnable {
    private String dbName;
    private String stockId;
    private Connection connection;
    private Logger logger;

    public TencentRealTimeThread(String dbName, String stockId) {
        super();
        this.dbName = dbName;
        this.stockId = stockId;
        this.logger = Logger.getLogger(TencentRealTimeThread.class);
        logger.info("TencentRealTimeThread::construct: Tencent Realtime Thread for " + stockId
                + " init done.");
    }

    @Override
    public void run() {
        logger.debug("TencentRealTimeThread::run: Tencent Realtime Thread for " + stockId + " entry.");
        ICapitalPoint point;
        TencentRealTimeAdapterComponent adapter = new TencentRealTimeAdapterComponent(stockId);
//        connection = StockDatebaseFactory.getInstance(dbName);
        ConnectionManager cm = ConnectionManager.getInstance();
        connection = cm.getConnection(dbName);
        if(connection == null)
        {
            System.out.println(stockId + " connection MySql failure.");
            logger.warn("TencentRealTimeThread::run: connection MySql failure for " + stockId);
            return;
        }
        
        try {
            CapitalPointDao dao = new CapitalPointDao(connection, stockId);
            dao.initDb();
            point = adapter.latestQuotation();
            if(point == null)
            {
//                System.out.println(stockId + " httpquery failure.");
                logger.warn("TencentRealTimeThread::run: httpquery failure for " + stockId);
                return;
            }
            Date lastDate = dao.lastDate();
            if(!lastDate.equals(point.getIndex()))
            {
                dao.save(point);
                dao.flush();
            }
        } catch (SQLException e) {
            logger.warn("TencentRealTimeThread::run: Tencent Realtime " + stockId + " Thread db operation failure with " + e.toString());
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
//                connection.close();
                cm.closeConnection(dbName, connection);
            } catch (SQLException e) {
                logger.warn("TencentRealTimeThread::run: release db connection for " + stockId + " failure with " + e.toString());
            }
        }
        
        logger.debug("TencentRealTimeThread::run: Tencent Realtime Thread for " + stockId + " exit!");
    }

}

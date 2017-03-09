package com.pureblue.quant.quantAPI;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.pureblue.quant.TencentAPI.TencentRealTimeAdapterComponent;
import com.pureblue.quant.dao.CapitalPointDao;
import com.pureblue.quant.dao.ICapitalPoint;
import com.pureblue.quant.dao.StockDatebaseFactory;

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
        try {
            connection = StockDatebaseFactory.getInstance(dbName);
            CapitalPointDao dao = new CapitalPointDao(connection, stockId);
            dao.initDb();
            point = adapter.latestQuotation();
            dao.save(point);
            dao.flush();
        } catch (SQLException e) {
            logger.warn("TencentRealTimeThread::run: Tencent Realtime " + stockId + " Thread db operation failure with " + e.toString());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.warn("TencentRealTimeThread::run: release db connection for " + stockId + " failure with " + e.toString());
            }
        }
        
        logger.debug("TencentRealTimeThread::run: Tencent Realtime Thread for " + stockId + " exit!");
    }

}

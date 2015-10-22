package main;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import dao.CapitalPointDao;
import dao.ICapitalPoint;
import dao.StockDatebaseFactory;
import Tencent.TencentRealTimeAdapterComponent;

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
        logger.info("TencentRealTimeThread::run: Tencent Realtime Thread for " + stockId
                + " entry.");
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
            logger.warn("TencentRealTimeThread::run: Tencent Realtime " + stockId + " Thread db operation failure with "
                    + e.toString());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.warn("TencentRealTimeThread::run: release db connection for " + stockId + " failure with "
                        + e.toString());
            }
        }
        
        /*catch (Exception e) {
            logger.error("TencentRealTimeThread::run: Tencent Realtime " + stockId + " Thread exception with "
                    + e.toString());
        } */
        
        logger.info("TencentRealTimeThread::run: Tencent Realtime Thread for " + stockId
                + " exit!");
    }

}

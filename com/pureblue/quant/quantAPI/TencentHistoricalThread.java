package com.pureblue.quant.quantAPI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.pureblue.quant.ConnectionPool.ConnectionManager;
import com.pureblue.quant.TencentAPI.TencentHistoryAdaptor;
import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.dao.OHLCPointDao;
import com.pureblue.quant.model.BarSize;
import com.pureblue.quant.model.SymbolFormat;

public class TencentHistoricalThread implements Runnable{
    private String dbName;
    private String stockId, symbol;
    private Connection connection;
    private boolean flagFetchAll = false;
    private ConnectionManager cm;
    private Logger logger;
    private TencentHistoryAdaptor adapter;

    public TencentHistoricalThread(String dbName, String stockId) {
        super();
        this.dbName = dbName;
        this.stockId = stockId;
        this.symbol = SymbolFormat.tencentSymbolFormat(stockId);;
        this.cm = ConnectionManager.getInstance();
        this.logger = Logger.getLogger(TencentHistoricalThread.class);
        this.adapter = new TencentHistoryAdaptor();
        logger.info("TencentHistoricalThread::construct: Tencent history Thread for " + stockId
                + " init done.");
    }

    public void fetchAll(){
        List<IOHLCPoint> info = null;
        try{
            info = adapter.historicalBars(symbol);
            if(info.size() == 0)
                return;
            connection = cm.getConnection(dbName);
            OHLCPointDao ohlcPointDao = new OHLCPointDao(connection);
            ohlcPointDao.initDb(stockId);
            ohlcPointDao.deleteAll(stockId);
            saveOHLCPoints(info, ohlcPointDao);
        }catch (Exception e) {
            logger.warn("TencentHistoricalThread::fetchAll: Tencent history fetch failure with "+e.toString());
        }finally {
            try {
                cm.closeConnection(dbName, connection);
            } catch (SQLException e) {
                logger.warn("TencentHistoricalThread::fetchAll: Tencent history connection release failure with "+e.toString());
            }
        }
    }

    public void fetchSlice() {
        logger.debug("TencentHistoricalThread::fetchSlice for " +stockId + " entry.");
        List<IOHLCPoint> info = null;
        Date startDateTime;
        Date endDateTime = new Date();
        long days = endDateTime.getTime()/BarSize.ONE_DAY.getDurationInMs();
        endDateTime.setTime(days * BarSize.ONE_DAY.getDurationInMs() - BarSize.EIGHT_HOURS.getDurationInMs());

        try{
            connection = cm.getConnection(dbName);
            OHLCPointDao ohlcPointDao = new OHLCPointDao(connection);
            ohlcPointDao.initDb(stockId);
            startDateTime = ohlcPointDao.lastDate(stockId);
            long startDays = startDateTime.getTime()/BarSize.ONE_DAY.getDurationInMs();

//            TimePeriod.getWeekOfDate(startDateTime) == 6 &&
//            days-startDays <= 2 &&

            if (startDateTime.compareTo(endDateTime)<=0)
            {
                info = adapter.updateLatestBar(symbol, startDateTime, endDateTime, BarSize.ONE_DAY);
                saveOHLCPoints(info, ohlcPointDao);
            }
        }catch (Exception e) {
            logger.warn("TencentHistoricalThread::fetchAll: Tencent history fetch failure with "+e.toString());
        }finally {
            try {
                cm.closeConnection(dbName, connection);
            } catch (SQLException e) {
                logger.warn("TencentHistoricalThread::fetchAll: Tencent history connection release failure with "+e.toString());
            }
        }
        logger.debug("TencentHistoricalThread::fetchSlice for " +stockId + " exit.");
    }

    @Override
    public void run() {
        logger.debug(Thread.currentThread().getName() + " " +stockId + " start!!");
        if(flagFetchAll)
            fetchAll();
        else
            fetchSlice();
        logger.debug(Thread.currentThread().getName() + " " +stockId + " end!!");
    }

    private void saveOHLCPoints(List<IOHLCPoint> info, OHLCPointDao ohlcPointDao) throws SQLException {
        Iterator<IOHLCPoint> itr = info.iterator();
        while(itr.hasNext()){
            ohlcPointDao.save(stockId, itr.next());
        }
        ohlcPointDao.flush();
    }

    public void setFetchMode(boolean mode){
        flagFetchAll = mode;
    }
}

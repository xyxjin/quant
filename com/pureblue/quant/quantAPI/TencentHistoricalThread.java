package com.pureblue.quant.quantAPI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.pureblue.quant.ConnectionPool.ConnectionManager;
import com.pureblue.quant.TencentAPI.TencentHistoryAdaptor;
import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.dao.OHLCPointDao;
import com.pureblue.quant.model.BarSize;
import com.pureblue.quant.model.STKktype;
import com.pureblue.quant.model.SymbolFormat;

public class TencentHistoricalThread implements Runnable{
    private String dbName;
    private String stockId, symbol;
    private Connection connection;
    private boolean flagFetchAll = false;
    private ConnectionManager cm;
    private Logger logger;
    private TencentHistoryAdaptor adapter;
    private STKktype ktype;

    public TencentHistoricalThread(String dbName, String stockId, STKktype ktype) {
        super();
        this.dbName = dbName;
        this.stockId = stockId;
        this.ktype = ktype;
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

    private boolean checkWeekendNeedRunning(Date startDateTime, Date endDateTime)
    {
        Calendar scal = Calendar.getInstance();
        scal.setTime(startDateTime);
        Calendar ecal = Calendar.getInstance();
        ecal.setTime(endDateTime);
/*
        System.out.println(startDateTime.toLocaleString());
        System.out.println(endDateTime.toLocaleString());
        System.out.println("start day: " + scal.get(Calendar.DAY_OF_YEAR));
        System.out.println("end day: " + ecal.get(Calendar.DAY_OF_YEAR));

        System.out.println(scal.get(Calendar.DAY_OF_WEEK));
        System.out.println(ecal.get(Calendar.DAY_OF_WEEK));
*/
        int days = ecal.get(Calendar.DAY_OF_YEAR) - scal.get(Calendar.DAY_OF_YEAR);
        if(ecal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && days == 1)
        {
            logger.info("TencentHistoricalThread::checkWeekendNeedRunning for " +stockId + " needn't update.");
            return false;
        }
        if(ecal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && days == 2)
        {
            logger.info("TencentHistoricalThread::checkWeekendNeedRunning for " +stockId + " needn't update.");
            return false;
        }
        return true;
    }

    private String createTableName() {
        String tableName = stockId;
        switch(ktype) {
            case DAY:
                break;
            case WEEK:
                tableName = stockId + "_week";
                break;
            case M30:
                tableName = stockId + "_m30";
                break;
            case M60:
                tableName = stockId + "_m60";
                break;
            default:
                break;
        }
        return tableName;
    }

    public void fetchSlice() {
        logger.debug("TencentHistoricalThread::fetchSlice for " +stockId + " entry.");
        List<IOHLCPoint> info = null;
        Date startDateTime;
        Date endDateTime = new Date();
        long days = endDateTime.getTime()/BarSize.ONE_DAY.getDurationInMs();
        endDateTime.setTime(days * BarSize.ONE_DAY.getDurationInMs() + BarSize.EIGHT_HOURS.getDurationInMs());

        try{
            connection = cm.getConnection(dbName);
            connection.setAutoCommit(false);
            OHLCPointDao ohlcPointDao = new OHLCPointDao(connection);
            ohlcPointDao.initDb(createTableName());
            startDateTime = ohlcPointDao.lastDate(createTableName());


            if (startDateTime.compareTo(endDateTime)<=0 && checkWeekendNeedRunning(startDateTime, endDateTime))
            {
//                info = adapter.updateLatestBar(symbol, startDateTime, endDateTime, BarSize.ONE_DAY);
                info = adapter.updateLatestBar(symbol, ktype, startDateTime, endDateTime);
                saveOHLCPoints(info, ohlcPointDao);
            }
        }catch (Exception e) {
            logger.warn("TencentHistoricalThread::fetchSlice: Tencent history fetch failure with "+e.toString());
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
        Random rm = new Random();
        try {
            Thread.sleep(rm.nextInt(10)*100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            ohlcPointDao.save(createTableName(), itr.next());
        }
        ohlcPointDao.flush();
    }

    public void setFetchMode(boolean mode){
        flagFetchAll = mode;
    }
}

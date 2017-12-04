package com.pureblue.quant.quantAPI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.pureblue.quant.ConnectionPool.ConnectionManager;
import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.dao.OHLCPointDao;
import com.pureblue.quant.main.portfolio;
import com.pureblue.quant.model.BarSize;
import com.pureblue.quant.model.DataType;
import com.pureblue.quant.model.IContract;
import com.pureblue.quant.model.TimePeriod;
import com.pureblue.quant.yahooAPI.YahooFinanceAdapterComponent;

public class YahooHistoricalThread implements Runnable {
    private String dbName;
    private String stockId;
    private ConnectionManager cm;
    private Connection connection;
    private Logger logger;

    public YahooHistoricalThread(String dbName, String stockId) {
        super();
        this.dbName = dbName;
        this.stockId = stockId;
        this.cm = ConnectionManager.getInstance();
        this.logger = Logger.getLogger(YahooHistoricalThread.class);
    }

    @Override
    public void run() {
        logger.debug("YahooHistoricalThread::run: Yahoo history thread for " + stockId + " entry!!");
        List<IOHLCPoint> info = null;
        Currency currency = Currency.getInstance(Locale.CHINA);
        Date startDateTime;
        Date endDateTime = new Date();
        long days = endDateTime.getTime() / BarSize.ONE_DAY.getDurationInMs();
        endDateTime.setTime(days * BarSize.ONE_DAY.getDurationInMs() - BarSize.EIGHT_HOURS.getDurationInMs());

        IContract contract = new portfolio(stockId, currency);
        YahooFinanceAdapterComponent adapter = new YahooFinanceAdapterComponent(stockId);

        try {
//            connection = StockDatebaseFactory.getInstance(dbName);
            connection = cm.getConnection(dbName);
            OHLCPointDao ohlcPointDao = new OHLCPointDao(connection);
            ohlcPointDao.initDb(stockId);
            startDateTime = ohlcPointDao.lastDate(stockId);
            logger.info("YahooHistoricalThread::run: The last date in DB is: " + startDateTime.toString() + " for stock= " + stockId);
            long startDays = startDateTime.getTime() / BarSize.ONE_DAY.getDurationInMs();
            boolean flagFetch = true;
            if (TimePeriod.getWeekOfDate(startDateTime) == 6 && days - startDays <= 2)
                flagFetch = false;
            if (flagFetch && startDateTime.before(endDateTime)) {
                info = adapter.historicalBars(contract, startDateTime, endDateTime, BarSize.ONE_DAY, DataType.BID_ASK, true, null);
                if(null == info){
                    logger.error("YahooHistoricalThread::run: Yahoo history thread for " + stockId + " fetch null points!");
                    return;
                }
                Iterator<IOHLCPoint> itr = info.iterator();
                logger.info("YahooHistoricalThread::run: Yahoo history fetch successfully for stock " + stockId);
                while (itr.hasNext()) {
                    ohlcPointDao.save(stockId, itr.next());
                }
                ohlcPointDao.flush();
                logger.info("YahooHistoricalThread::run: Yahoo history save successfully for stock " + stockId);
            }
        } catch (SQLException | NullPointerException e) {
            logger.warn("YahooHistoricalThread::run: Yahoo history thread for " + stockId + " SQL operation failure with error info " + e.toString());
        }
        finally {
            try {
                cm.closeConnection(dbName, connection);
                logger.info("YahooHistoricalThread::run: Yahoo history thread close the jdbc connection!");
            } catch (SQLException | NullPointerException e) {
                logger.warn("YahooHistoricalThread::run: Yahoo history thread for " + stockId + " close SQL db operation failure with error info " + e.toString());
            }
        }
        logger.debug("YahooHistoricalThread::run: Yahoo history thread for " + stockId + " exit!");
    }
}

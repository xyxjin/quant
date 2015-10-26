package com.pureblue.quant.quantAPI;

import java.sql.Connection;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import com.pureblue.quant.TencentAPI.HushenMarket;
import com.pureblue.quant.dao.StockDatebaseFactory;

public class YahooHistoryStock implements IGeneralAPI {
    public void fetchActions() {
        Logger logger = Logger.getLogger(getClass());
        logger.info("YahooHistoryStock::fetchActions: fetch Tencent web real time stock quotes entry.");
        Connection connection = StockDatebaseFactory.getInstance("test");
        if (null == connection) {
            logger.fatal("YahooHistoryStock::fetchActions: connect to SQL database failure.");
            return;
        }
        HushenMarket market = new HushenMarket(connection);
        Set<String> quotes = market.findAll();

        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        for (String symbol : quotes) {
            YahooHistoricalThread t = new YahooHistoricalThread("test", symbol);
            pool.execute(t);
        }
        pool.shutdown();
        try {
            while (!pool.isTerminated()) {
                Thread.sleep(5000);
                logger.info("YahooHistoryStock::fetchActions: complete task count: " + pool.getCompletedTaskCount());
            }
            if (!pool.awaitTermination(15, TimeUnit.MINUTES)) {
                logger.fatal("YahooHistoryStock::fetchActions: Tencent real quote has been timeout!");
            }
        } catch (InterruptedException e) {
            logger.fatal("YahooHistoryStock::fetchActions: waiting the tencent real time web fetch complete with error info: " + e.toString());
        }
        logger.info("YahooHistoryStock::fetchActions: fetch Tencent web real time stock quotes exit!");

    }

}

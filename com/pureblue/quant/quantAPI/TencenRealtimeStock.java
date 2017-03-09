package com.pureblue.quant.quantAPI;

import java.sql.Connection;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.pureblue.quant.TencentAPI.HushenMarket;
import com.pureblue.quant.dao.StockDatebaseFactory;
import com.pureblue.quant.model.SymbolFormat;

public class TencenRealtimeStock implements IGeneralAPI {

    private ThreadPoolExecutor pool = null;
    private int poolSize = 0;
    private Logger logger = null;

    public TencenRealtimeStock(int poolSize) {
        super();
        this.poolSize = poolSize;
        this.pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
        this.logger = Logger.getLogger(getClass());
    }

    public void fetchActions() {
        logger.info("TencenRealtimeStock::fetchActions: fetch Tencent web real time stock quotes entry.");
        Connection connection = StockDatebaseFactory.getInstance("test");
        if (null == connection) {
            logger.fatal("TencenRealtimeStock::fetchActions: connect to SQL database failure.");
            return;
        }
        HushenMarket market = new HushenMarket(connection);
        Set<String> quotes = market.findAll();

        for (String symbol : quotes) {
            TencentRealTimeThread t = new TencentRealTimeThread("tencentreal", SymbolFormat.tencentSymbolFormat(symbol));
            pool.execute(t);
        }
        pool.shutdown();
        try {
            while (!pool.isTerminated()) {
                Thread.sleep(5000);
                logger.info("TencenRealtimeStock::fetchActions: complete task count: " + pool.getCompletedTaskCount());
            }
            if (!pool.awaitTermination(15, TimeUnit.MINUTES)) {
                logger.fatal("TencenRealtimeStock::fetchActions: Tencent real quote has been timeout!");
            }
        } catch (InterruptedException e) {
            logger.fatal("TencenRealtimeStock::fetchActions: waiting the tencent real time web fetch complete with error info: " + e.toString());
        }
        logger.info("TencenRealtimeStock::fetchActions: fetch Tencent web real time stock quotes exit!");
    }

    @Override
    public void stop() {        
    }
    
    public ThreadPoolExecutor getPool() {
        return pool;
    }

    public void setPool(ThreadPoolExecutor pool) {
        this.pool = pool;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}

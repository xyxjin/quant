package com.pureblue.quant.main;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.pureblue.quant.ConnectionPool.ConnectionManager;
import com.pureblue.quant.model.HushenMarketQuotes;
import com.pureblue.quant.model.SymbolFormat;
import com.pureblue.quant.quantAPI.TencentRealTimeThread;
import com.pureblue.quant.util.DelistedSymbol;

public class TencentRealtimeExample {

    public static void main(String[] args) {

        Logger logger = Logger.getLogger(TencentRealtimeExample.class);

        Set<String> quotes = HushenMarketQuotes.stockSymbolFromEastMoney();
        DelistedSymbol.RemoveDelistedSymbol(quotes);
//        Set<String> quotes = new HashSet<String>();
//        quotes.add("600030");

        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        for (String symbol : quotes) {
            TencentRealTimeThread t = new TencentRealTimeThread("tencentdaily",
                                                SymbolFormat.tencentSymbolFormat(symbol));
            pool.execute(t);
        }
        pool.shutdown();
        while(!pool.isTerminated()){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("complete task count: "+pool.getCompletedTaskCount());
        }
/*
        try {
            if (!pool.awaitTermination(15, TimeUnit.MINUTES)) {
                logger.fatal("Tencent real quote has been timeout!!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        ConnectionManager.getInstance().destroy();
        System.out.println("done!!!!!!!!!!!!!!!!!!!");

    }

}

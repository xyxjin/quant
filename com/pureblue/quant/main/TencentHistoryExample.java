package com.pureblue.quant.main;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.pureblue.quant.quantAPI.TencentHistoricalThread;

public class TencentHistoryExample {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(TencentHistoryExample.class);
//        Set<String> quotes = HushenMarketQuotes.stockSymbolFromEastMoney();
        Set<String> quotes = new HashSet<String>();
        quotes.add("600570");
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        for(String symbol : quotes)
        {
            TencentHistoricalThread t = new TencentHistoricalThread("tencentstockquotes", symbol);
            t.setFetchMode(false);
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
        System.out.println("done!!!!!!!!!!!!!!!!!!!");
    }
}

package com.pureblue.quant.main;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.pureblue.quant.model.HushenMarketQuotes;
import com.pureblue.quant.model.STKktype;
import com.pureblue.quant.model.SymbolFormat;
import com.pureblue.quant.quantAPI.TencentHistoricalThread;
import com.pureblue.quant.quantAPI.TencentRealTimeThread;
import com.pureblue.quant.util.DelistedSymbol;

public class TencentHistoryExample {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(TencentHistoryExample.class);
        Set<String> quotes = HushenMarketQuotes.stockSymbolFromEastMoney();

//        Set<String> quotes = new HashSet<String>();
//        quotes.add("300172");
//        quotes.add("300319");
//        quotes.add("603823");
        DelistedSymbol.RemoveDelistedSymbol(quotes);

        List<String> symbolList = new LinkedList<String>();

        for(String symbol : quotes)
        {
            symbolList.add(symbol);
        }

        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        for(int i=0; i<symbolList.size(); i++)
        {
            TencentHistoricalThread t1 = new TencentHistoricalThread("tencentstockquotes", symbolList.get(i), STKktype.M30);
            TencentHistoricalThread t2 = new TencentHistoricalThread("tencentstockquotes", symbolList.get(i), STKktype.DAY);
            TencentRealTimeThread t3 = new TencentRealTimeThread("tencentdaily", SymbolFormat.tencentSymbolFormat(symbolList.get(i)));
            pool.execute(t1);
            pool.execute(t2);
            pool.execute(t3);

            if(i%100 == 0)
            {
                System.out.println("Active task count: "+ pool.getActiveCount() +
                                   " in " + pool.getTaskCount() +
                                   " completed " + pool.getCompletedTaskCount());
                while(pool.getActiveCount() > 40 || (i*3 - pool.getCompletedTaskCount()) >= 99)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("After sleep 1s, Active task count: "+ pool.getActiveCount() +
                                       " in " + pool.getTaskCount() +
                                       " completed " + pool.getCompletedTaskCount());
                }
            }
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

package com.pureblue.quant.quantAPI;

import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.apache.log4j.Logger;

import com.pureblue.quant.TencentAPI.TencentRealTimeAdapterComponent;
import com.pureblue.quant.dao.CapitalPointDao;
import com.pureblue.quant.dao.ICapitalPoint;
import com.pureblue.quant.dao.StockDatebaseFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution 
public class SimpleJob implements Job {

    private Logger logger;
    private Connection connection = null;
//    private static index = "";
    
    public SimpleJob() {
//        System.out.println("------- Starting construct ----------------");
        this.logger = Logger.getLogger(SimpleJob.class);
    }
    
    private boolean isInterrupted(JobExecutionContext context) {
        return context.getJobDetail().getJobDataMap()
               .containsKey("interrupt")
               && (Boolean) context.getJobDetail().getJobDataMap()
               .get("interrupt");
    }
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        System.out.println("------- Starting execute ----------------");
        JobDataMap data = context.getJobDetail().getJobDataMap();  
        String stockId = data.getString("symbol");
        String dbName = data.getString("dbTable");
        String lastIndex = data.containsKey("index") ? data.getString("index"): "";
        
        logger.debug("TencentRealTimeThread::execute: Tencent Realtime Thread for " + stockId + " entry.");
        
        if(isInterrupted(context))
        {
            logger.info("TencentRealTimeThread::execute: Tencent Realtime Thread for " + stockId + " has been Interrupted.");
            return;
        }
        
        ICapitalPoint point;
        TencentRealTimeAdapterComponent adapter = new TencentRealTimeAdapterComponent(stockId);
        try {
            point = adapter.latestQuotation();
            
            if(null == point || 0.0 == point.getTurnoverRate())
            {
                System.out.println("interrupt null done " + stockId);
                data.put("interrupt", true);
                return;
            }
            
            String index = point.getIndex().toString();
            data.put("index", index);
            System.out.println(stockId + index);
            System.out.println(stockId + lastIndex);
            if(lastIndex.equals(index))
            {
                System.out.println("interrupt stop done " + stockId);
                data.put("interrupt", true);
                return;
            }
            
            connection = StockDatebaseFactory.getInstance(dbName);
            CapitalPointDao dao = new CapitalPointDao(connection, stockId);
            System.out.println("not done!");
            dao.initDb();
            dao.save(point);
            dao.flush();
        } catch (SQLException e) {
            logger.warn("TencentRealTimeThread::run: Tencent Realtime " + stockId + " Thread db operation failure with " + e.toString());
        } finally {
            try {
                if(null != connection)
                    connection.close();
            } catch (SQLException e) {
                logger.warn("TencentRealTimeThread::run: release db connection for " + stockId + " failure with " + e.toString());
            }
        }
        
        logger.debug("TencentRealTimeThread::run: Tencent Realtime Thread for " + stockId + " exit!");
    }
}

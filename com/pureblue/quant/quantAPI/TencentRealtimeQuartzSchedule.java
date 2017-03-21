package com.pureblue.quant.quantAPI;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.apache.log4j.Logger;

import com.pureblue.quant.TencentAPI.HushenMarket;
import com.pureblue.quant.dao.StockDatebaseFactory;
import com.pureblue.quant.model.HushenMarketQuotes;
import com.pureblue.quant.model.SymbolFormat;

public class TencentRealtimeQuartzSchedule {

    public void run() throws Exception {
        Logger log = Logger.getLogger(TencentRealtimeQuartzSchedule.class);
        
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        JobDetail job;
        CronTrigger trigger;
        
        Set<String> quotes = HushenMarketQuotes.stockSymbolFromEastMoney();
        for (String symbol : quotes) {
            log.info("add " + symbol + " to schedule.");
            job = newJob(TencentRealTimeQuartzJob.class).withIdentity("job"+symbol, "group").build();
            job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat(symbol));  
            job.getJobDataMap().put("dbName", "tencentreal");
            trigger = newTrigger().withIdentity("trigger1", "group").withSchedule(cronSchedule("0 0-59/2 10,13-14 * * MON-FRI"))
                        .build();
            sched.scheduleJob(job, trigger);
            
            trigger = newTrigger().withIdentity("trigger2", "group").withSchedule(cronSchedule("0 30-59/2 9 * * MON-FRI"))
                        .build();
            sched.scheduleJob(job, trigger);
            
            trigger = newTrigger().withIdentity("trigger3", "group").withSchedule(cronSchedule("0 0-30/2 11 * * MON-FRI"))
                        .build();
            sched.scheduleJob(job, trigger);
        }
        
        log.info("TencentRealtimeQuartzSchedule::run: ----- Starting Scheduler -----.");

        sched.start();

        while(true)
        {
            try {
                Thread.sleep(300L * 1000L);
              } catch (Exception e) {
                  log.error("TencentRealtimeQuartzSchedule::run: exception happened while scheduling waiting.");
                  break;
              }
        }

        System.out.println("------- Shutting Down ---------------------");
        log.info("TencentRealtimeQuartzSchedule::run: ----- Shutting Down -----");

        sched.shutdown(true);
        System.out.println("------- Shutdown Complete -----------------");
        log.info("TencentRealtimeQuartzSchedule::run: ----- Shutdown Complete -----");

        SchedulerMetaData metaData = sched.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
        log.info("TencentRealtimeQuartzSchedule::run: " + "Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
        
    }
    
    public static void main(String[] args) throws Exception {

        TencentRealtimeQuartzSchedule example = new TencentRealtimeQuartzSchedule();
        example.run();
      }
}

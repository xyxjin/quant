package com.pureblue.quant.quantAPI;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pureblue.quant.model.SymbolFormat;

public class CronTriggerTencentRealtime {

    public void run() throws Exception {
        Logger log = LoggerFactory.getLogger(CronTriggerTencentRealtime.class);
        
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("600030"));  
        job.getJobDataMap().put("dbTable", "tencentreal");
        CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule("0/5 * *  * * ?"))
            .build();
        Date ft = sched.scheduleJob(job, trigger);
        
        job = newJob(SimpleJob.class).withIdentity("job2", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("000979"));  
        job.getJobDataMap().put("dbTable", "tencentreal");
        
        trigger = newTrigger().withIdentity("trigger2", "group1").withSchedule(cronSchedule("0/5 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
       
//        sched.getListenerManager().addTriggerListener(arg0, arg1);
//        sched.getListenerManager().addTriggerListener(arg0);
              
/*        job = newJob(SimpleJob.class).withIdentity("job3", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("600570"));  
        job.getJobDataMap().put("dbTable", "tencentreal"); 
        
        trigger = newTrigger().withIdentity("trigger3", "group1").withSchedule(cronSchedule("0 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
        job = newJob(SimpleJob.class).withIdentity("job4", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("000858"));  
        job.getJobDataMap().put("dbTable", "tencentreal");
        
        trigger = newTrigger().withIdentity("trigger4", "group1").withSchedule(cronSchedule("5 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
        job = newJob(SimpleJob.class).withIdentity("job5", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("002230"));  
        job.getJobDataMap().put("dbTable", "tencentreal");
        
        trigger = newTrigger().withIdentity("trigger5", "group1").withSchedule(cronSchedule("10 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
        
        job = newJob(SimpleJob.class).withIdentity("job6", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("601398"));  
        job.getJobDataMap().put("dbTable", "tencentreal");  
        
        trigger = newTrigger().withIdentity("trigger6", "group1").withSchedule(cronSchedule("15 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
        
        job = newJob(SimpleJob.class).withIdentity("job7", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("300059"));  
        job.getJobDataMap().put("dbTable", "tencentreal");  
        
        trigger = newTrigger().withIdentity("trigger7", "group1").withSchedule(cronSchedule("20 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
        
        job = newJob(SimpleJob.class).withIdentity("job8", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("300104"));  
        job.getJobDataMap().put("dbTable", "tencentreal");
        
        trigger = newTrigger().withIdentity("trigger8", "group1").withSchedule(cronSchedule("25 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
        
        job = newJob(SimpleJob.class).withIdentity("job9", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("000156"));  
        job.getJobDataMap().put("dbTable", "tencentreal");
        
        trigger = newTrigger().withIdentity("trigger9", "group1").withSchedule(cronSchedule("30 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
        
        job = newJob(SimpleJob.class).withIdentity("job10", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("002396"));  
        job.getJobDataMap().put("dbTable", "tencentreal");
        
        trigger = newTrigger().withIdentity("trigger10", "group1").withSchedule(cronSchedule("35 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
        
        
        job = newJob(SimpleJob.class).withIdentity("job11", "group1").build();
        job.getJobDataMap().put("symbol", SymbolFormat.tencentSymbolFormat("600837"));  
        job.getJobDataMap().put("dbTable", "tencentreal");
        
        trigger = newTrigger().withIdentity("trigger12", "group1").withSchedule(cronSchedule("40 * *  * * ?")).build();
        sched.scheduleJob(job, trigger);
*/        
        
        System.out.println("------- Starting Scheduler ----------------");

        // All of the jobs have been added to the scheduler, but none of the
        // jobs
        // will run until the scheduler has been started
        sched.start();

        System.out.println("------- Started Scheduler -----------------");

        System.out.println("------- Waiting five minutes... ------------");
/*        try {
          // wait five minutes to show jobs
          Thread.sleep(300L * 1000L);
          // executing...
        } catch (Exception e) {
          //
        }*/
        while(true)
        {
            try {
                // wait five minutes to show jobs
                Thread.sleep(300L * 1000L);
                System.out.println("------- Waiting five minutes... ------------");
                // executing...
              } catch (Exception e) {
                break;
              }
        }

        System.out.println("------- Shutting Down ---------------------");

        sched.shutdown(true);

        System.out.println("------- Shutdown Complete -----------------");

        SchedulerMetaData metaData = sched.getMetaData();
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }
    
    public static void main(String[] args) throws Exception {

        CronTriggerTencentRealtime example = new CronTriggerTencentRealtime();
        example.run();
      }
}

package com.pureblue.quant.main;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FiveMinuteTask{    
    ScheduledExecutorService _Timer;
    ScheduledFuture<?> _TimerFuture;
    long startMillis, endMillis;
    final static int TIMER_IN_SECONDS = 5;

    public FiveMinuteTask(long startMills, long endMills) {
        super();
        this._Timer = Executors.newScheduledThreadPool(2);
        this._TimerFuture = null;
        this.startMillis = startMills;
        this.endMillis = endMills;
    }

    private boolean startTimer() {
        try {
            if (_TimerFuture != null) {
                //cancel execution of the future task (TimerPopTask())
                //If task is already running, do not interrupt it.
                _TimerFuture.cancel(false);
            }
            
            long timeMillis = System.currentTimeMillis();
            
            System.out.println((startMillis - timeMillis)/1000);
            _TimerFuture = _Timer.scheduleAtFixedRate(new TimerPopTask(), (startMillis-timeMillis)/1000, TIMER_IN_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean stopTimer() {
        try {
            if (_TimerFuture != null) {
                //cancel execution of the future task (TimerPopTask())
                //If task is already running, interrupt it here.
                TimerPopClose stopper = new TimerPopClose(_TimerFuture);
                long timeMillis = System.currentTimeMillis();
                _Timer.schedule(stopper, endMillis-timeMillis, TimeUnit.MILLISECONDS);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private class TimerPopTask implements Runnable  {
        public void run ()   {  
            System.out.println("Timer running...");
        }  
    }
    
    private class TimerPopClose implements Runnable{
        ScheduledFuture<?> _TimerFuture;
        public TimerPopClose(ScheduledFuture<?> _TimerFuture){
            this._TimerFuture = _TimerFuture;
        }
        public void run() {
            _TimerFuture.cancel(true);
            System.out.println("Cancel...");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 2);
        calendar.set(Calendar.SECOND, 00);
        Date date = calendar.getTime();
        System.out.println(date.getTime());
        long startMillis = date.getTime();
        
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 18);
        calendar.set(Calendar.SECOND, 00);
        date = calendar.getTime();
        System.out.println(date.getTime());
        long endMillis = date.getTime();
        
        FiveMinuteTask test = new FiveMinuteTask(startMillis, endMillis);  
        test.startTimer();
//        Thread.sleep(500000);
        test.stopTimer();
//        while(!test._Timer.isShutdown()){
//            System.out.println("shutdown...");
//        }
        while(!test._Timer.isTerminated()){
            Thread.sleep(50000);
            System.out.println("terminated...");
        }
        System.out.println("main exit...");
        }
    
}
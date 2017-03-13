package com.pureblue.quant.main;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class scheduleFirstTime {
    Timer timer;

    public scheduleFirstTime() {
        Date time = getTime();
        System.out.println("指定时间time=" + time);
        timer = new Timer();
        timer.schedule(new TimerTaskTest02(), time);
    }

    public Date getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 39);
        calendar.set(Calendar.SECOND, 00);
        Date time = calendar.getTime();
        return time;
    }

    public static void main(String[] args) {
        new scheduleFirstTime();
    }

public class TimerTaskTest02 extends TimerTask {
    @Override
    public void run() {
        System.out.println("指定时间执行线程任务...");
    }
}

}
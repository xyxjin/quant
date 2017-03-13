package com.pureblue.quant.main;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class scheduleRepeat {
    Timer timer;

    public scheduleRepeat() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTaskTest03(), 1000, 2000);
    }

    public static void main(String[] args) {
        new scheduleRepeat();
    }

public class TimerTaskTest03 extends TimerTask {
    @Override
    public void run() {
        Date date = new Date(this.scheduledExecutionTime());
        System.out.println("本次执行该线程的时间为：" + date);
    }
}

}
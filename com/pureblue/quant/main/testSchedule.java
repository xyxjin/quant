package com.pureblue.quant.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class testSchedule {    
    private  ScheduledExecutorService scheduExec;       
    public long start;
    testSchedule(){
        this.scheduExec =  Executors.newScheduledThreadPool(2);
        this.start = System.currentTimeMillis();    
        }
    public void timerOne(){  
        scheduExec.schedule(new Runnable() {
            public void run() {     
                System.out.println("timerOne invoked .....");
//                throw new RuntimeException();        
                }        
            },1000,TimeUnit.MILLISECONDS);    
        }        
    public void timerTwo(){
        scheduExec.scheduleAtFixedRate(new Runnable() {  
            public void run() {                
                System.out.println("timerTwo invoked ....."); 
                }        
            },2000,500,TimeUnit.MILLISECONDS);   
        }        
    public static void main(String[] args) {     
        testSchedule test = new testSchedule();  
        test.timerOne();    
        test.timerTwo();   
        }
    }
package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class testThreadPool{

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		int count = 100;
		System.out.println("task: " + (count+1)); 
		while(count != 0){
			System.out.println("task: " + (count+1)); 
			Thread t = new MyThread();
			pool.execute(t);
			count--;
		}
		/*
		Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        Thread t4 = new MyThread();
        Thread t5 = new MyThread();
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        */
        pool.shutdown();
	}

}

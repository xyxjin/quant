package main;

public class MyThread extends Thread {
    @Override
    public void run() {
    	try {
			sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println(Thread.currentThread().getName() + "正在执行。。。");
    }
}

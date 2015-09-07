package main;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


class MyThread1 implements java.lang.Runnable
{
    private int threadId;
    private List<Integer> lock;
    public MyThread1(int id, List<Integer> obj)
    {
        this.threadId = id;
        this.lock = obj;
    }
    @Override
    public  void run() 
    {
    	for (int i = 0; i < 100; ++i)
        {
            System.out.println("Thread ID: " + this.threadId + " : " + i);
        }
        synchronized(lock)
        {
        	for (int i = 0; i < 100; ++i)
            {
        		lock.add(i);
//                System.out.println("Thread ID: " + this.threadId + " : " + i);
            }
        }
    }
}
public class testSync
{
    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException
    {
//        Object obj = new Object();
    	List<Integer> obj = new LinkedList<Integer>();
    	Future future[] = new Future[10];
    	ExecutorService threadExecutor = Executors.newCachedThreadPool(); 
        for (int i = 0; i < 10; ++i)
        {
        	MyThread1 t = new MyThread1(i, obj);
        	future[i] = threadExecutor.submit(t);
//        	t.join();
//        	MyThread1 t = new MyThread1(i, obj);
//        	threadExecutor.execute(t);
        }
        for (int i = 0; i < 10; ++i)
        {
	    	try {
				future[i].get();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        threadExecutor.shutdown();
        System.out.println("done!!");
        System.out.println(obj.toString());
    }
}
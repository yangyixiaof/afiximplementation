package demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example {
	
	static int x=0;
	static Lock lock1 = new ReentrantLock();
	
	public static void main(String[] args)
	{
			MyThread t = new MyThread();
			t.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			lock1.lock();
			x=0;
			lock1.unlock();
	}
	
	static class MyThread extends Thread
	{
		
		public void run()
		{
			lock1.lock();
			x++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println(1/x);
			lock1.unlock();
		}
	}
	
}

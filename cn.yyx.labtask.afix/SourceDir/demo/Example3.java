package demo;

import java.util.concurrent.locks.Lock;

public class Example3 {
	
	static int x=0;
	
	public static void main(String[] args)
	{
			MyThread t = new MyThread();
			t.start();
			cn.yyx.labtask.afix.LockPool.lock1.lock();
			x=0; // line 11
			cn.yyx.labtask.afix.LockPool.lock1.unlock();
	}
	
	static class MyThread extends Thread
	{
		
		public void run()
		{
			cn.yyx.labtask.afix.LockPool.lock1.lock();
			x++; // line 19
			System.out.println(1/x); // line 20
			cn.yyx.labtask.afix.LockPool.lock1.unlock();
		}
	}
	
}

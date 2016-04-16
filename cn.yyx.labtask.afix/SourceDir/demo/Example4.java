package demo;

import java.util.concurrent.locks.Lock;

public class Example4 {
	
	static int x=0;
	static Object lock = new Object();
	
	public static void main(String[] args)
	{
			MyThread t = new MyThread();
			t.start();
			cn.yyx.labtask.afix.LockPool.lock1.lock();
			cn.yyx.labtask.afix.LockPool.lock2.lock();
			cn.yyx.labtask.afix.LockPool.lock3.lock();
			cn.yyx.labtask.afix.LockPool.lock4.lock();
			x=0; // line 11
			cn.yyx.labtask.afix.LockPool.lock4.unlock();
			cn.yyx.labtask.afix.LockPool.lock3.unlock();
			cn.yyx.labtask.afix.LockPool.lock2.unlock();
			cn.yyx.labtask.afix.LockPool.lock1.unlock();
	}
	
	static class MyThread extends Thread
	{
		
		public void run()
		{
			synchronized (lock)
			{
				cn.yyx.labtask.afix.LockPool.lock1.lock();
				cn.yyx.labtask.afix.LockPool.lock2.lock();
				cn.yyx.labtask.afix.LockPool.lock3.lock();
				cn.yyx.labtask.afix.LockPool.lock4.lock();
				x++; // line 19
			}
			System.out.println(1/x); // line 20
			cn.yyx.labtask.afix.LockPool.lock4.unlock();
			cn.yyx.labtask.afix.LockPool.lock3.unlock();
			cn.yyx.labtask.afix.LockPool.lock2.unlock();
			cn.yyx.labtask.afix.LockPool.lock1.unlock();
		}
	}
	
}
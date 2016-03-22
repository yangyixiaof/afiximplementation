package demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example {

	static int x = 0;

	public static void main(String[] args) {
		MyThread t = new MyThread();
		t.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		cn.yyx.labtask.afix.LockPool.lock1.lock();
		x = 0;
		cn.yyx.labtask.afix.LockPool.lock1.unlock();
	}

	static class MyThread extends Thread {
		public void run() {
			cn.yyx.labtask.afix.LockPool.lock1.lock();
			x++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println(1 / x);
			cn.yyx.labtask.afix.LockPool.lock1.unlock();
		}
	}

}

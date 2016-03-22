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
		x = 0;
	}

	static class MyThread extends Thread {
		public void run() {
			x++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println(1 / x);
		}
	}

}

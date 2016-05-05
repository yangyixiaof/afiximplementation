package demo;

public class Example4 {
	
	static int x=0;
	static Object lock = new Object();
	
	public static void main(String[] args)
	{
			MyThread t = new MyThread();
			t.start();
			x=0; // line 11
	}
	
	static class MyThread extends Thread
	{
		
		public void run()
		{
			synchronized (lock)
			{
				x++; // line 19
			}
			System.out.println(1/x); // line 20
		}
	}
	
}

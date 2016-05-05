package demo;

public class Example3 {
	
	static int x=0;
	
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
			x++; // line 19
			System.out.println(1/x); // line 20
		}
	}
	
}

package cn.yyx.labtask.afix.irengine;

public class Test {
	
	public void heihei()
	{
		int x = 0;
		{
			if (x == 1)
			{
				x = 6;
			} else {
				x = 5;
			}
			System.err.println(x);
		}
		
		java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(Integer.MAX_VALUE);
		System.err.println(semaphore);
	}
	
}

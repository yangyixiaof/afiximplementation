package cn.yyx.labtask.afix.irengine;

public class Ha {
	
	public static void main(String[] args) {
		synchronized (haha) {
			synchronized (haha) {
				synchronized (cn.yyx.labtask.afix.LockPool.lock1) {
					System.err.println("hei hei.");
				}
			}
		}
	}
	
}
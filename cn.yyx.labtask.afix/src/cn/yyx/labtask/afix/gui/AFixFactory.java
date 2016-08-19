package cn.yyx.labtask.afix.gui;

import java.util.ArrayList;
import java.util.List;

public class AFixFactory {
	
	static List<AFixEntity> list = new ArrayList<AFixEntity>();
	
	static {
		// AFixEntity o = new AFixEntity("lock_mock", "lock", "no_exist.java:-1", "mock.no_exist.java:-1");
		// list.add(o);
	}
	
	public static void CLear()
	{
		list.clear();
	}
	
	public static void AddEntry(AFixEntity afe)
	{
		list.add(afe);
	}
	
}
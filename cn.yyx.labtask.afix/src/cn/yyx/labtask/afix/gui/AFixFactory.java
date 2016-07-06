package cn.yyx.labtask.afix.gui;

import java.util.ArrayList;
import java.util.List;

public class AFixFactory {
	
	static List<AFixEntity> list = new ArrayList<AFixEntity>();
	
	static {
		AFixEntity o = new AFixEntity("lock1", "lock", "asd.java:12", "cn.yyx.test.asd.java:12");
		list.add(o);
	}
	
}
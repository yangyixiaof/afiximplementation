package cn.yyx.labtask.afix.gui;

import java.util.ArrayList;
import java.util.List;

public class AFixFactory {
	
	static List<AFixEntity> list = new ArrayList<AFixEntity>();
	
	static {
		AFixEntity o = new AFixEntity("asd", "lock", "asd:12");
		list.add(o);
	}
	
}
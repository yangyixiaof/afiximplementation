package cn.yyx.labtask.afix.raceinput;

import java.util.TreeMap;

public class PCRPool {
	
	TreeMap<String, TreeMap<String, Boolean>> pcrs = new TreeMap<String, TreeMap<String, Boolean>>();
	
	public void AddRacePair(String one, String two)
	{
		AddOneDirection(one, two);
		AddOneDirection(two, one);
	}
	
	private void AddOneDirection(String one, String two)
	{
		TreeMap<String, Boolean> o1 = pcrs.get(one);
		if (o1 == null)
		{
			o1 = new TreeMap<String, Boolean>();
			pcrs.put(one, o1);
		}
		o1.put(two, true);
	}
	
}
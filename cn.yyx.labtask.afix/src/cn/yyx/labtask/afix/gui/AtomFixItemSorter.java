package cn.yyx.labtask.afix.gui;

public class AtomFixItemSorter implements Comparable<AtomFixItemSorter> {
	
	String name = null;
	Integer idx = 0;
	
	public AtomFixItemSorter(String name, Integer idx) {
		this.name = name;
		this.idx = idx;
	}
	
	@Override
	public int compareTo(AtomFixItemSorter o) {
		if (idx == o.idx)
		{
			return name.compareTo(name);
		}
		return idx.compareTo(o.idx);
	}
	
}
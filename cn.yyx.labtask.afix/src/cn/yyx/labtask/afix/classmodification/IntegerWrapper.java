package cn.yyx.labtask.afix.classmodification;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class IntegerWrapper implements Comparable<IntegerWrapper> {
	
	private ModifyContent mc = null;
	private Set<IntegerWrapper> connected = new HashSet<IntegerWrapper>();
	private int iv = -1;
	
	public IntegerWrapper(int iv) {
		this.setIv(iv);
	}
	
	public int getIv() {
		return iv;
	}

	private void setIv(int iv) {
		this.iv = iv;
	}
	
	public void AddConnectedIntegerWrapper(IntegerWrapper iw, boolean needreverse)
	{
		connected.add(iw);
		if (needreverse)
		{
			iw.AddConnectedIntegerWrapper(this, false);
		}
	}
	
	public Iterator<IntegerWrapper> GetIterator()
	{
		return connected.iterator();
	}

	@Override
	public int compareTo(IntegerWrapper o) {
		return new Integer(iv).compareTo(new Integer(o.iv));
	}

	public ModifyContent getMc() {
		return mc;
	}

	public void setMc(ModifyContent mc) {
		this.mc = mc;
	}
	
}
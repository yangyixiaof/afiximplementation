package cn.yyx.labtask.afix.classmodification;

import java.util.LinkedList;
import java.util.List;

public class ModifyContent implements Comparable<ModifyContent> {
	
	private int reallockidx = -1;
	private IntegerWrapper lockidx = null;
	private List<OneModify> oms = new LinkedList<OneModify>();
	
	public ModifyContent(IntegerWrapper lockidx) {
		this.setLockidx(lockidx);
		lockidx.setMc(this);
	}
	
	public List<OneModify> getOms() {
		return oms;
	}

	public IntegerWrapper getLockidx() {
		return lockidx;
	}

	private void setLockidx(IntegerWrapper lockidx) {
		this.lockidx = lockidx;
	}

	@Override
	public int compareTo(ModifyContent o) {
		return lockidx.compareTo(o.lockidx);
	}

	public int getReallockidx() {
		if (reallockidx == -1)
		{
			return lockidx.getIv();
		}
		return reallockidx;
	}

	public void setReallockidx(int reallockidx) {
		this.reallockidx = reallockidx;
	}
	
}
package cn.yyx.labtask.afix.classmodification;

import java.util.LinkedList;
import java.util.List;

public class ModifyContent {
	
	private IntegerWrapper lockidx = null;
	private List<OneModify> oms = new LinkedList<OneModify>();
	
	public ModifyContent(IntegerWrapper lockidx) {
		this.setLockidx(lockidx);
	}
	
	public List<OneModify> getOms() {
		return oms;
	}

	public IntegerWrapper getLockidx() {
		return lockidx;
	}

	public void setLockidx(IntegerWrapper lockidx) {
		this.lockidx = lockidx;
	}
	
}
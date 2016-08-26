package cn.yyx.labtask.afix.classmodification;

import java.util.LinkedList;
import java.util.List;

public class IntegerWrapper {
	
	private List<IntegerWrapper> connected = new LinkedList<IntegerWrapper>();
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

	public List<IntegerWrapper> getConnected() {
		return connected;
	}
	
}
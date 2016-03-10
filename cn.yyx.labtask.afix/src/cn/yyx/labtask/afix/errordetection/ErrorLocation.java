package cn.yyx.labtask.afix.errordetection;

public class ErrorLocation {
	
	private String sig = null;
	private int bytecodel = -1;
	
	public ErrorLocation(String sig, int bytecodel)
	{
		this.setSig(sig);
		this.setBytecodel(bytecodel);
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public int getBytecodel() {
		return bytecodel;
	}

	public void setBytecodel(int bytecodel) {
		this.bytecodel = bytecodel;
	}
	
}

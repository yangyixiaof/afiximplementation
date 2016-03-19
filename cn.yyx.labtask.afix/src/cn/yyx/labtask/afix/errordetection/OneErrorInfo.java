package cn.yyx.labtask.afix.errordetection;

public class OneErrorInfo {
	
	private ErrorTrace p = null;
	private ErrorTrace c = null;
	private ErrorTrace r = null;
	
	public OneErrorInfo(ErrorTrace p, ErrorTrace c, ErrorTrace r) {
		this.setP(p);
		this.setC(c);
		this.setR(r);
	}

	public ErrorTrace getP() {
		return p;
	}

	public void setP(ErrorTrace p) {
		this.p = p;
	}

	public ErrorTrace getC() {
		return c;
	}

	public void setC(ErrorTrace c) {
		this.c = c;
	}

	public ErrorTrace getR() {
		return r;
	}

	public void setR(ErrorTrace r) {
		this.r = r;
	}
	
}
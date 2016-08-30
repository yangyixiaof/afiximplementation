package cn.yyx.labtask.afix.patchgeneration;

import com.ibm.wala.ssa.SSAInstruction;

public class AFixSSABlockExtraInfo {
	
	private SSAInstruction upboundinst = null;
	private SSAInstruction downboundinst = null;
	
	public AFixSSABlockExtraInfo(SSAInstruction upboundinst, SSAInstruction downboundinst) {
		this.setUpboundinst(upboundinst);
		this.setDownboundinst(downboundinst);
	}

	public SSAInstruction getUpboundinst() {
		return upboundinst;
	}

	public void setUpboundinst(SSAInstruction upboundinst) {
		this.upboundinst = upboundinst;
	}

	public SSAInstruction getDownboundinst() {
		return downboundinst;
	}

	public void setDownboundinst(SSAInstruction downboundinst) {
		this.downboundinst = downboundinst;
	}
	
	@Override
	public String toString() {
		return "Up:" + upboundinst + ";Down:" + downboundinst;
	}
	
}
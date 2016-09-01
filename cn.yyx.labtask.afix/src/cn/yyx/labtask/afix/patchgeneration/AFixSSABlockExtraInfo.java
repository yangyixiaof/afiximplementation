package cn.yyx.labtask.afix.patchgeneration;

import com.ibm.wala.ssa.SSAInstruction;

public class AFixSSABlockExtraInfo {
	
	private SSAInstruction upboundinst = null;
	private SSAInstruction downboundinst = null;
	private String varname = null;
	
	public AFixSSABlockExtraInfo(SSAInstruction upboundinst, SSAInstruction downboundinst, String varname) {
		this.setUpboundinst(upboundinst);
		this.setDownboundinst(downboundinst);
		this.setVarname(varname);
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

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}
	
}
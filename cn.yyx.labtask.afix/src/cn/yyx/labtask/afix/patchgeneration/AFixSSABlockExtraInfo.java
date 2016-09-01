package cn.yyx.labtask.afix.patchgeneration;

import com.ibm.wala.ssa.SSAInstruction;

public class AFixSSABlockExtraInfo {
	
	private SSAInstruction upboundinst = null;
	private String upvarname = null;
	private SSAInstruction downboundinst = null;
	private String downvarname = null;
	
	public AFixSSABlockExtraInfo(SSAInstruction upboundinst, String upvarname, SSAInstruction downboundinst, String downvarname) {
		this.setUpboundinst(upboundinst);
		this.setUpvarname(upvarname);
		this.setDownboundinst(downboundinst);
		this.setDownvarname(downvarname);
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
		return "Up:" + upboundinst + ";UpVar:" + upvarname + ";Down:" + downboundinst + "DownVar:" + downvarname;
	}

	public String getUpvarname() {
		return upvarname;
	}

	public void setUpvarname(String upvarname) {
		this.upvarname = upvarname;
	}

	public String getDownvarname() {
		return downvarname;
	}

	public void setDownvarname(String downvarname) {
		this.downvarname = downvarname;
	}
	
}
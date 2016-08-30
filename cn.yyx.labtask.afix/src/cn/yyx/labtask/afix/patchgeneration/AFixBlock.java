package cn.yyx.labtask.afix.patchgeneration;

import com.ibm.wala.ssa.ISSABasicBlock;

public class AFixBlock {
	
	private ISSABasicBlock rbk = null;
	private AFixSSABlockExtraInfo asbei = null;
	
	public AFixBlock(ISSABasicBlock rbk, AFixSSABlockExtraInfo asbei) {
		this.setISSABasicBlock(rbk);
		this.setAFixSSABlockExtraInfo(asbei);
	}
	
	public ISSABasicBlock getISSABasicBlock() {
		return rbk;
	}
	
	public void setISSABasicBlock(ISSABasicBlock rbk) {
		this.rbk = rbk;
	}
	
	public AFixSSABlockExtraInfo getAFixSSABlockExtraInfo() {
		return asbei;
	}
	
	public void setAFixSSABlockExtraInfo(AFixSSABlockExtraInfo asbei) {
		this.asbei = asbei;
	}
	
	@Override
	public String toString() {
		return "BasicBlock:" + rbk + ";BlockExtraInfo:" + asbei;
	}
	
}
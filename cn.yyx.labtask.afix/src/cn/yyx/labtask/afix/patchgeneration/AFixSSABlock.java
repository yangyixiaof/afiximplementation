package cn.yyx.labtask.afix.patchgeneration;

import com.ibm.wala.ssa.ISSABasicBlock;

public class AFixSSABlock {
	
	private ISSABasicBlock ssablock = null;
	private int upbound = -1;
	private int downbound = -1;
	
	public AFixSSABlock(ISSABasicBlock ssablock, int upbound, int downbound) {
		this.setSsablock(ssablock);
		this.setUpbound(upbound);
		this.setDownbound(downbound);
	}

	public ISSABasicBlock getSsablock() {
		return ssablock;
	}

	public void setSsablock(ISSABasicBlock ssablock) {
		this.ssablock = ssablock;
	}

	public int getUpbound() {
		return upbound;
	}

	public void setUpbound(int upbound) {
		this.upbound = upbound;
	}

	public int getDownbound() {
		return downbound;
	}

	public void setDownbound(int downbound) {
		this.downbound = downbound;
	}
	
}

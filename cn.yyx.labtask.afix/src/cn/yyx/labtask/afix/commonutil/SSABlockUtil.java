package cn.yyx.labtask.afix.commonutil;

import java.util.Map;

import com.ibm.wala.ssa.ISSABasicBlock;

import cn.yyx.labtask.afix.patchgeneration.AFixSSABlockExtraInfo;

public class SSABlockUtil {
	
	public static void SetSSABlockInfo(Map<ISSABasicBlock, AFixSSABlockExtraInfo> ssablockinfo, ISSABasicBlock ib, AFixSSABlockExtraInfo ibextrainfo) {
		AFixSSABlockExtraInfo extrainfo = ssablockinfo.get(ib);
		if (extrainfo == null) {
			ssablockinfo.put(ib, ibextrainfo);
		} else {
			if (extrainfo.getDownboundinst() == null) {
				extrainfo.setDownboundinst(ibextrainfo.getDownboundinst());
			} else {
				if (ibextrainfo.getDownboundinst() != null)
				{
					if (extrainfo.getDownboundinst().iindex < ibextrainfo.getDownboundinst().iindex)
					{
						extrainfo.setDownboundinst(ibextrainfo.getDownboundinst());
						extrainfo.setDownvarname(ibextrainfo.getDownvarname());
					}
				}
			}
			if (extrainfo.getUpboundinst() == null) {
				extrainfo.setUpboundinst(ibextrainfo.getUpboundinst());
			} else {
				if (ibextrainfo.getUpboundinst() != null)
				{
					if (extrainfo.getUpboundinst().iindex > ibextrainfo.getUpboundinst().iindex)
					{
						extrainfo.setUpboundinst(ibextrainfo.getUpboundinst());
						extrainfo.setUpvarname(ibextrainfo.getUpvarname());
					}
				}
			}
		}
	}
	
}
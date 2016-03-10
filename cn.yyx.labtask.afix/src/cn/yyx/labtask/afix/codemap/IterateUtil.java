package cn.yyx.labtask.afix.codemap;

import java.util.Iterator;

import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;

public class IterateUtil {
	
	public void IterateCFG(SSACFG sfg)
	{
		BasicBlock ent = sfg.entry();
		Iterator<ISSABasicBlock> sir = sfg.getSuccNodes(ent);
		while (sir.hasNext())
		{
			ISSABasicBlock ib = sir.next();
			System.out.println("Next IB:"+ib+";-o-;");
		}
	}
	
}

package cn.yyx.labtask.afix.commonutil;

import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeBT.MethodData;

public class DebugUtil {
	
	public void PrintInstructions(MethodData d)
	{
		IInstruction[] iis = d.getInstructions();
		for (IInstruction ii : iis)
		{
			System.out.println("ii:" + ii);
		}
	}
	
}

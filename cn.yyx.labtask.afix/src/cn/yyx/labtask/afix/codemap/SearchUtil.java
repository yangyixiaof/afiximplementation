package cn.yyx.labtask.afix.codemap;

import java.util.Iterator;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSAInstruction;

public class SearchUtil {
	
	/*public static ISSABasicBlock GetBasicBlockAccordingToLineNumberInBytecode(int bytecodeLineNumber, IR ir) throws InvalidClassFileException
	{
		// SSACFG sfg = ir.getControlFlowGraph();
		IBytecodeMethod method = (IBytecodeMethod) ir.getMethod();
		Iterator<SSAInstruction> iir = ir.iterateAllInstructions();
		int mostcloseidx = -1;
		SSAInstruction csi = null;
		while (iir.hasNext())
		{
			SSAInstruction si = iir.next();
			// System.out.println("sicnt:"+si+";siindex:"+);
			int idx = si.iindex;
			if (idx >= 0)
			{
				int bytecodeIndex = method.getBytecodeIndex(idx);
				if ((bytecodeIndex < bytecodeLineNumber) && (mostcloseidx < bytecodeIndex))
				{
					mostcloseidx = bytecodeIndex;
					csi = si;
				}
				// int sourceLineNum = method.getLineNumber(bytecodeIndex);
				// System.out.println("bytecodeIndex:"+bytecodeIndex+";sourceIndex:"+sourceLineNum);
				if (bytecodeIndex == bytecodeLineNumber)
				{
					return ir.getBasicBlockForInstruction(si);
				}
			}
		}
		return ir.getBasicBlockForInstruction(csi);
	}*/
	
	public static ISSABasicBlock GetBasicBlockAccordingToLineNumberInSourcecode(int sourceLineNumber, IR ir) throws InvalidClassFileException
	{
		// SSACFG sfg = ir.getControlFlowGraph();
		IBytecodeMethod method = (IBytecodeMethod) ir.getMethod();
		Iterator<SSAInstruction> iir = ir.iterateAllInstructions();
		int mostcloseidx = -1;
		SSAInstruction csi = null;
		while (iir.hasNext())
		{
			SSAInstruction si = iir.next();
			// System.out.println("sicnt:"+si+";siindex:"+);
			int idx = si.iindex;
			if (idx >= 0)
			{
				int bytecodeIndex = method.getBytecodeIndex(idx);
				int sourceLineNum = method.getLineNumber(bytecodeIndex);
				if ((sourceLineNum < sourceLineNumber) && (mostcloseidx < sourceLineNum))
				{
					mostcloseidx = sourceLineNum;
					csi = si;
				}
				// int sourceLineNum = method.getLineNumber(bytecodeIndex);
				// System.out.println("bytecodeIndex:"+bytecodeIndex+";sourceIndex:"+sourceLineNum);
				if (sourceLineNum == sourceLineNumber)
				{
					return ir.getBasicBlockForInstruction(si);
				}
			}
		}
		return ir.getBasicBlockForInstruction(csi);
	}
	
}
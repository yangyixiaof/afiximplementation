package cn.yyx.labtask.afix.codemap;

import java.util.Iterator;

import com.ibm.wala.cast.java.loader.JavaSourceLoaderImpl.ConcreteJavaMethod;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSAInstruction;

import cn.yyx.labtask.afix.patchgeneration.AFixBlock;
import cn.yyx.labtask.afix.patchgeneration.AFixSSABlockExtraInfo;

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
	
	public static final int UpAndDownDirection = 1;
	public static final int UpDirection = 2;
	public static final int DownDirection = 3;
	
	public static AFixBlock GetAFixBlockAccordingToLineNumberInSourcecode(final String racevar, final int sourceLineNumber, final IR ir, final int direction) throws InvalidClassFileException
	{
		// SSACFG sfg = ir.getControlFlowGraph();
		ConcreteJavaMethod method = (ConcreteJavaMethod) ir.getMethod();// IBytecodeMethod
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
				// int bytecodeIndex = method.getBytecodeIndex(idx);
				int currSourceLine = method.getLineNumber(idx);// bytecodeIndex
				
				// System.err.println("sicnt:"+si+";sourceLineNum:"+sourceLineNum);
				
				if ((currSourceLine < sourceLineNumber) && (mostcloseidx < currSourceLine))
				{
					mostcloseidx = currSourceLine;
					csi = si;
				}
				// int sourceLineNum = method.getLineNumber(bytecodeIndex);
				// System.out.println("bytecodeIndex:"+bytecodeIndex+";sourceIndex:"+sourceLineNum);
				if (currSourceLine == sourceLineNumber)
				{
					csi = si;
					break;
					// return ir.getBasicBlockForInstruction(si);
				}
			}
		}
		AFixBlock result = null;
		ISSABasicBlock issabk = ir.getBasicBlockForInstruction(csi);
		switch (direction) {
		case UpAndDownDirection:
			result = new AFixBlock(issabk, new AFixSSABlockExtraInfo(csi, racevar, csi, racevar));
			break;
		case UpDirection:
			result = new AFixBlock(issabk, new AFixSSABlockExtraInfo(csi, racevar, null, null));
			break;
		case DownDirection:
			result = new AFixBlock(issabk, new AFixSSABlockExtraInfo(null, null, csi, racevar));
			break;
		default:
			System.err.println("What the fuck Direction!");
			System.exit(1);
			break;
		}
		return result;
	}
	
	public static ISSABasicBlock GetBasicBlockAccordingToLineNumberInSourcecode(final int sourceLineNumber, IR ir) throws InvalidClassFileException
	{
		// SSACFG sfg = ir.getControlFlowGraph();
		ConcreteJavaMethod method = (ConcreteJavaMethod) ir.getMethod();// IBytecodeMethod
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
				// int bytecodeIndex = method.getBytecodeIndex(idx);
				int currSourceLine = method.getLineNumber(idx);// bytecodeIndex
				
				// System.err.println("sicnt:"+si+";sourceLineNum:"+sourceLineNum);
				
				if ((currSourceLine < sourceLineNumber) && (mostcloseidx < currSourceLine))
				{
					mostcloseidx = currSourceLine;
					csi = si;
				}
				// int sourceLineNum = method.getLineNumber(bytecodeIndex);
				// System.out.println("bytecodeIndex:"+bytecodeIndex+";sourceIndex:"+sourceLineNum);
				if (currSourceLine == sourceLineNumber)
				{
					return ir.getBasicBlockForInstruction(si);
				}
			}
		}
		return ir.getBasicBlockForInstruction(csi);
	}
	
}
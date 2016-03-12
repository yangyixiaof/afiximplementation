package cn.yyx.labtask.afix.patchgeneration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;

import cn.yyx.labtask.afix.errordetection.ErrorTrace;

public class OnePatch {
	
	ErrorTrace et = null;
	String methodsig = null;
	Set<ISSABasicBlock> protectednodes = null;
	IR ir = null;
	SSACFG cfg = null;
	List<Integer> insertbeginidxs = null;
	List<Integer> insertendidxs = null;
	
	public OnePatch(ErrorTrace et, String methodsig, Set<ISSABasicBlock> protectednodes, IR ir, SSACFG cfg) {
		this.et = et;
		this.methodsig = methodsig;
		this.protectednodes = protectednodes;
		this.ir = ir;
		this.cfg = cfg;
	}
	
	private void AddLockBeforeIndex(Integer idx)
	{
		insertbeginidxs.add(idx);
	}
	
	private void AddUnlockAfterIndex(Integer idx)
	{
		insertendidxs.add(idx);
	}
	
	private void CheckGenerateLockUnlockSolutions() throws InvalidClassFileException
	{
		if (insertbeginidxs == null)
		{
			insertbeginidxs = new LinkedList<Integer>();
			insertendidxs = new LinkedList<Integer>();
			BasicBlock ent = cfg.entry();
			if (protectednodes.contains(ent))
			{
				AddLockBeforeIndex(GetBasicBlockBeforePosition(ent, ir));
			}
			BasicBlock ext = cfg.exit();
			if (protectednodes.contains(ext))
			{
				AddUnlockAfterIndex(GetBasicBlockAfterPosition(ext, ir));
			}
			Set<ISSABasicBlock> visited = new HashSet<ISSABasicBlock>();
			visited.add(ent);
			visited.add(ext);
			Set<ISSABasicBlock> blockprelock = new HashSet<ISSABasicBlock>();
			Set<ISSABasicBlock> blockafterunlock = new HashSet<ISSABasicBlock>();
			IterateBlockToAddLockAndUnlock(ent, cfg, protectednodes, visited, blockprelock, blockafterunlock, ir);
		}
	}
	
	private void IterateBlockToAddLockAndUnlock(ISSABasicBlock now, SSACFG cfg, Set<ISSABasicBlock> protectnodes, Set<ISSABasicBlock> visited, Set<ISSABasicBlock> blockprelock, Set<ISSABasicBlock> blockafterunlock, IR ir) throws InvalidClassFileException
	{
		if (visited.contains(now))
		{
			return;
		}
		visited.add(now);
		Iterator<ISSABasicBlock> itr = cfg.getSuccNodes(now);
		while (itr.hasNext())
		{
			ISSABasicBlock ibb = itr.next();
			if ((protectnodes.contains(now)) && (!protectnodes.contains(ibb)))
			{
				if (!blockafterunlock.contains(now))
				{
					AddUnlockAfterIndex(GetBasicBlockAfterPosition(now, ir));
					blockafterunlock.add(now);
				}
			}
			if ((!protectnodes.contains(now)) && (protectnodes.contains(ibb)))
			{
				if (!blockprelock.contains(ibb))
				{
					AddLockBeforeIndex(GetBasicBlockBeforePosition(ibb, ir));
					blockprelock.add(ibb);
				}
			}
			IterateBlockToAddLockAndUnlock(ibb, cfg, protectnodes, visited, blockprelock, blockafterunlock, ir);
		}
	}
	
	private Integer GetBasicBlockBeforePosition(ISSABasicBlock bbk, IR ir) throws InvalidClassFileException {
		int iidx = bbk.getFirstInstructionIndex();
		IBytecodeMethod method = (IBytecodeMethod) ir.getMethod();
		int bytecodeIndex = method.getBytecodeIndex(iidx);
		return bytecodeIndex;
	}
	
	private Integer GetBasicBlockAfterPosition(ISSABasicBlock bbk, IR ir) throws InvalidClassFileException {
		int iidx = bbk.getLastInstructionIndex();
		IBytecodeMethod method = (IBytecodeMethod) ir.getMethod();
		int bytecodeIndex = method.getBytecodeIndex(iidx);
		return bytecodeIndex;
	}
	
	public Iterator<Integer> GetInsertPosEndIterator() throws InvalidClassFileException
	{
		CheckGenerateLockUnlockSolutions();
		return insertendidxs.iterator();
	}
	
	public Iterator<Integer> GetInsertPosBeginIterator() throws InvalidClassFileException
	{
		CheckGenerateLockUnlockSolutions();
		return insertbeginidxs.iterator();
	}
	
	/**
	 * 
	 * @param iop
	 * @return true intersected and merged; false un_intersected and un_merged.
	 */
	public boolean JudgeIntersectAndMerge(OnePatch iop) {
		boolean intersected = false;
		// situation1: in same method and intersected.
		if (methodsig.equals(iop.methodsig))
		{
			Iterator<ISSABasicBlock> itr = iop.protectednodes.iterator();
			while (itr.hasNext())
			{
				ISSABasicBlock ibb = itr.next();
				if (protectednodes.contains(ibb))
				{
					intersected = true;
					break;
				}
			}
			if (intersected)
			{
				itr = iop.protectednodes.iterator();
				while (itr.hasNext())
				{
					ISSABasicBlock ibb = itr.next();
					protectednodes.add(ibb);
				}
			}
		}
		// situation2: in different method and be wrapped.
		if (!(methodsig.equals(iop.methodsig)))
		{
			// sub_situation1:
			
			// sub_situation2:
			
		}
		return intersected;
	}
	
}
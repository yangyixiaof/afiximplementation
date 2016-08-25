package cn.yyx.labtask.afix.patchgeneration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ibm.wala.cast.java.loader.JavaSourceLoaderImpl.ConcreteJavaMethod;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;
import com.ibm.wala.ssa.SSAInstruction;

import cn.yyx.labtask.afix.codemap.SearchUtil;
import cn.yyx.labtask.afix.errordetection.ErrorLocation;
import cn.yyx.labtask.afix.errordetection.ErrorTrace;

public class OnePatch implements Mergeable {
	
	ErrorTrace et = null;
	private String methodsig = null;
	Set<ISSABasicBlock> protectednodes = null;
	IR ir = null;
	SSACFG cfg = null;
	List<Integer> insertbeginidxs = null;
	List<Integer> insertendidxs = null;
	List<Integer> insertsourcebeginidxs = null;
	List<Integer> insertsourceendidxs = null;
	
	Map<ISSABasicBlock, AFixSSABlockExtraInfo> ssablockinfo = null;
	
	public OnePatch(ErrorTrace et, String methodsig, Set<ISSABasicBlock> protectednodes, IR ir, SSACFG cfg, Map<ISSABasicBlock, AFixSSABlockExtraInfo> ssablockinfo) {
		this.et = et;
		this.setMethodsig(methodsig);
		this.protectednodes = protectednodes;
		this.ir = ir;
		this.cfg = cfg;
		this.ssablockinfo = ssablockinfo;
	}
	
	private void AddLockBeforeIndex(Integer idx, Integer sourceidx)
	{
		insertbeginidxs.add(idx);
		insertsourcebeginidxs.add(sourceidx);
	}
	
	private void AddUnlockAfterIndex(Integer idx, Integer sourceidx)
	{
		insertendidxs.add(idx);
		insertsourceendidxs.add(sourceidx);
	}
	
	public void CheckGenerateLockUnlockSolutions() throws InvalidClassFileException
	{
		/*if (methodsig.startsWith("demo.Example.main([Ljava/lang/String;)V"))
		{
			System.out.println("just test.");
		}*/
		if (insertbeginidxs == null)
		{
			insertbeginidxs = new LinkedList<Integer>();
			insertendidxs = new LinkedList<Integer>();
			insertsourcebeginidxs = new LinkedList<Integer>();
			insertsourceendidxs = new LinkedList<Integer>();
			BasicBlock ent = cfg.entry();
			if (protectednodes.contains(ent))
			{
				AddLockBeforeIndex(GetBasicBlockBeforePosition(ent, ir), GetBasicBlockBeforeSourcePosition(ent, ir));
			}
			BasicBlock ext = cfg.exit();
			if (protectednodes.contains(ext))
			{
				AddUnlockAfterIndex(GetBasicBlockAfterPosition(ext, ir), GetBasicBlockAfterSourcePosition(ext, ir));
			}
			Set<ISSABasicBlock> visited = new HashSet<ISSABasicBlock>();
			Set<ISSABasicBlock> blockprelock = new HashSet<ISSABasicBlock>();
			Set<ISSABasicBlock> blockafterunlock = new HashSet<ISSABasicBlock>();
			IterateBlockToAddLockAndUnlock(ent, cfg, protectednodes, visited, blockprelock, blockafterunlock, ir, ent, ext);
		}
	}
	
	// ISSABasicBlock
	private void IterateBlockToAddLockAndUnlock(ISSABasicBlock now, SSACFG cfg, Set<ISSABasicBlock> protectnodes, Set<ISSABasicBlock> visited, Set<ISSABasicBlock> blockprelock, Set<ISSABasicBlock> blockafterunlock, IR ir, final BasicBlock ent, final BasicBlock ext) throws InvalidClassFileException
	{
		if (visited.contains(now))
		{
			return;
		}
		visited.add(now);
		Iterator<ISSABasicBlock> itr = cfg.getNormalSuccessors(now).iterator();// .getSuccNodes(now)
		while (itr.hasNext())
		{
			ISSABasicBlock ibb = itr.next();
			/*if (ibb == ext)
			{
				continue;
			}*/
			if ((protectnodes.contains(now)) && (!protectnodes.contains(ibb)))
			{
				if (!blockafterunlock.contains(now))
				{
					/*ISSABasicBlock bk = now;
					Collection<ISSABasicBlock> nsuccs = cfg.getNormalSuccessors(now);
					if (nsuccs.size() == 1)
					{
						bk = nsuccs.iterator().next();
						if (bk == ext || bk.getFirstInstructionIndex() != bk.getLastInstructionIndex())
						{
							bk = now;
						}
					}*/
					AddUnlockAfterIndex(GetBasicBlockAfterPosition(now, ir), GetBasicBlockAfterSourcePosition(now, ir));
					blockafterunlock.add(now);
				}
			}
			if ((!protectnodes.contains(now)) && (protectnodes.contains(ibb)))
			{
				if (!blockprelock.contains(ibb))
				{
					AddLockBeforeIndex(GetBasicBlockBeforePosition(ibb, ir), GetBasicBlockBeforeSourcePosition(ibb, ir));
					blockprelock.add(ibb);
				}
			}
			IterateBlockToAddLockAndUnlock(ibb, cfg, protectnodes, visited, blockprelock, blockafterunlock, ir, ent, ext);
		}
	}
	
	private Integer GetBasicBlockBeforePosition(ISSABasicBlock bbk, IR ir) throws InvalidClassFileException {
		int iidx = bbk.getFirstInstructionIndex();
		return iidx;
	}
	
	// ISSABasicBlockISSABasicBlock
	private Integer GetBasicBlockBeforeSourcePosition(ISSABasicBlock bbk, IR ir) throws InvalidClassFileException {
		int iidx = bbk.getFirstInstructionIndex();
		ConcreteJavaMethod method = (ConcreteJavaMethod) ir.getMethod();// IBytecodeMethod
		// int bytecodeIndex = method.getBytecodeIndex(iidx);
		int sourline = method.getLineNumber(iidx);// bytecodeIndex
		AFixSSABlockExtraInfo bbkinfo = ssablockinfo.get(bbk);
		if (bbkinfo != null && bbkinfo.getUpboundinst() != null)
		{
			sourline = method.getLineNumber(bbkinfo.getUpboundinst().iindex);
		}
		return sourline;
	}
	
	// ISSABasicBlock
	private Integer GetBasicBlockAfterPosition(ISSABasicBlock bbk, IR ir) throws InvalidClassFileException {
		int iidx = bbk.getLastInstructionIndex();
		/*SSAInstruction is = bbk.getLastInstruction();
		String content = is.toString();
		if (content.startsWith("return"))
		{
			iidx--;
		}*/
		return iidx;
	}
	
	private Integer GetBasicBlockAfterSourcePosition(ISSABasicBlock bbk, IR ir) throws InvalidClassFileException {
		int iidx = bbk.getLastInstructionIndex();
		ConcreteJavaMethod method = (ConcreteJavaMethod) ir.getMethod();// IBytecodeMethod
		// int bytecodeIndex = method.getBytecodeIndex(iidx);
		int sourline = method.getLineNumber(iidx);// bytecodeIndex
		int maxsl = GetMaxSourceLineOfBlock(bbk, method);
		if (sourline != maxsl)
		{
			sourline = maxsl;
		}
		AFixSSABlockExtraInfo bbkinfo = ssablockinfo.get(bbk);
		if (bbkinfo != null && bbkinfo.getDownboundinst() != null)
		{
			sourline = method.getLineNumber(bbkinfo.getDownboundinst().iindex);
		}
		// confirm.
		// SSAInstruction is = bbk.getLastInstruction();
		// String content = is.toString();
		// if (content.startsWith("return"))
		// {
		//	sourline--;
		// }
		return sourline;
	}
	
	private int GetMaxSourceLineOfBlock(ISSABasicBlock bbk, ConcreteJavaMethod method)
	{
		int maxs = 0;
		Iterator<SSAInstruction> itr = bbk.iterator();
		while (itr.hasNext())
		{
			SSAInstruction si = itr.next();
			int sourline = method.getLineNumber(si.iindex);
			if (maxs < sourline)
			{
				maxs = sourline;
			}
		}
		return maxs;
	}
	
	public Iterator<Integer> GetInsertPosEndIterator() throws InvalidClassFileException
	{
		// CheckGenerateLockUnlockSolutions();
		return insertendidxs.iterator();
	}
	
	public Iterator<Integer> GetInsertPosEndSourceIterator() throws InvalidClassFileException
	{
		// CheckGenerateLockUnlockSolutions();
		return insertsourceendidxs.iterator();
	}
	
	public Iterator<Integer> GetInsertPosBeginIterator() throws InvalidClassFileException
	{
		// CheckGenerateLockUnlockSolutions();
		return insertbeginidxs.iterator();
	}
	
	public Iterator<Integer> GetInsertPosBeginSourceIterator() throws InvalidClassFileException
	{
		// CheckGenerateLockUnlockSolutions();
		return insertsourcebeginidxs.iterator();
	}
	
	/**
	 * 
	 * @param iop
	 * @return true intersected and merged; false un_intersected and un_merged.
	 * @throws InvalidClassFileException 
	 */
	@Override
	public Mergeable Merge(Mergeable ioppara) throws InvalidClassFileException {
		OnePatch iop = (OnePatch)ioppara;
		boolean intersected = false;
		// situation1: in same method and intersected.
		if (getMethodsig().equals(iop.getMethodsig()))
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
				MergeSSABlockInfo(iop);
				return this;
			}
		}
		// situation2: in different method and be wrapped.
		if (!(getMethodsig().equals(iop.getMethodsig())))
		{
			// sub_situation1: this may in upper
			ErrorTrace tet = iop.et;
			Iterator<ErrorLocation> itr = tet.GetNegativeOrderIterator();
			while (itr.hasNext())
			{
				ErrorLocation tel = itr.next();
				if (getMethodsig().equals(tel.getSig()))
				{
					// ISSABasicBlock tbk = SearchUtil.GetBasicBlockAccordingToLineNumberInBytecode(tel.getBytecodel(), ir);
					ISSABasicBlock tbk = SearchUtil.GetBasicBlockAccordingToLineNumberInSourcecode(tel.getLine(), ir);
					if (protectednodes.contains(tbk))
					{
						intersected = true;
						return this;
					}
					break;
				}
			}
			// sub_situation2: this may in lower
			if (!intersected)
			{
				String tmsig = iop.getMethodsig();
				itr = et.GetNegativeOrderIterator();
				while (itr.hasNext())
				{
					ErrorLocation tel = itr.next();
					if (tmsig.equals(tel.getSig()))
					{
						// ISSABasicBlock tbk = SearchUtil.GetBasicBlockAccordingToLineNumberInBytecode(tel.getBytecodel(), iop.ir);
						ISSABasicBlock tbk = SearchUtil.GetBasicBlockAccordingToLineNumberInSourcecode(tel.getLine(), iop.ir);
						if (iop.protectednodes.contains(tbk))
						{
							intersected = true;
							return iop;
						}
						break;
					}
				}
			}
		}
		if (intersected != false)
		{
			System.err.println("intersected must be false at this place, serious error the system will exit.");
			new Exception().printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	private void MergeSSABlockInfo(OnePatch iop)
	{
		if (ssablockinfo == null) {
			ssablockinfo = iop.ssablockinfo;
		} else {
			if (iop.ssablockinfo != null)
			{
				ssablockinfo.putAll(iop.ssablockinfo);
			}
		}
	}
	
	public String getMethodsig() {
		return methodsig;
	}

	public void setMethodsig(String methodsig) {
		this.methodsig = methodsig;
	}
	
}
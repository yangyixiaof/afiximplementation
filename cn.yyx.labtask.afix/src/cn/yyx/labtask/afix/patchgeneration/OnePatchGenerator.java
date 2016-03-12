package cn.yyx.labtask.afix.patchgeneration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;
import com.ibm.wala.ssa.SSAOptions;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.io.FileProvider;
import com.ibm.wala.util.strings.StringStuff;

import cn.yyx.labtask.afix.codemap.SearchUtil;
import cn.yyx.labtask.afix.controlflow.AFixCallGraph;
import cn.yyx.labtask.afix.errordetection.ErrorLocation;
import cn.yyx.labtask.afix.errordetection.ErrorTrace;

public class OnePatchGenerator {
	
	ClassHierarchy cha = null;
	String appJar = null;
	ErrorLocation p = null;
	ErrorLocation c = null;
	ErrorLocation r = null;
	LinkedList<OnePatch> ops = null;
	boolean overlap = false;
	int overlapflag = 0; // 0:at the same level. 1:r is lower level. 2:r is upper level.
	
	public OnePatchGenerator(String jar, ErrorTrace p, ErrorTrace c, ErrorTrace r) throws Exception
	{
		this.appJar = jar;
		ErrorLocation pel = null;
		ErrorLocation cel = null;
		boolean stop = false;
		Iterator<ErrorLocation> pitr = p.GetNegativeOrderIterator();
		while (pitr.hasNext())
		{
			pel = pitr.next();
			Iterator<ErrorLocation> citr = c.GetNegativeOrderIterator();
			stop = false;
			while (citr.hasNext())
			{
				cel = citr.next();
				if (pel.InSameMethod(cel))
				{
					stop = true;
					break;
				}
			}
			if (stop)
			{
				break;
			}
		}
		if (stop)
		{
			this.p = pel;
			this.c = cel;
			// situation 1 : r and p is at same level.
			boolean issitu1 = false;
			ErrorLocation rel = r.GetNegativeOrderIterator().next();
			this.r = rel;
			if (pel.InSameMethod(rel))
			{
				int pidx = this.p.getBytecodel();
				int cidx = this.c.getBytecodel();
				int ridx = rel.getBytecodel();
				if ((pidx < ridx && ridx < cidx) || (cidx < ridx && ridx < pidx))
				{
					overlap = true;
					overlapflag = 0;
					issitu1 = true;
				}
			}
			// situation 2 : r is lower level.
			if (!issitu1)
			{
				ErrorLocation tel = null;
				Iterator<ErrorLocation> rir = r.GetNegativeOrderIterator();
				while (rir.hasNext())
				{
					tel = rir.next();
					if (pel.InSameMethod(tel))
					{
						int pidx = this.p.getBytecodel();
						int cidx = this.c.getBytecodel();
						int ridx = tel.getBytecodel();
						if ((pidx < ridx && ridx < cidx) || (cidx < ridx && ridx < pidx))
						{
							overlap = true;
							overlapflag = 1;
							this.r = tel;
						}
						break;
					}
				}
			}
			// situation 3 : r is upper level.
			/*boolean issitu3 = false;
			if (!issitu1 && !issitu2)
			{
				while (pitr.hasNext())
				{
					ErrorLocation tel = pitr.next();
					if (rel.InSameMethod(tel))
					{
						overlap = true;
						overlapflag = 2;
						issitu3 = true;
						this.p = tel;
						this.r = tel;
						break;
					}
				}
			}*/
		}
		else
		{
			throw new NoOneScopePatchException("p and c can not put in one (parent) method scope.");
		}
		if (AFixCallGraph.isDirectory(appJar)) {
			appJar = AFixCallGraph.findJarFiles(new String[] { appJar });
		}
		AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,
				(new FileProvider()).getFile(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
		cha = ClassHierarchy.make(scope);
	}
	
	/**
	 * 
	 * @return if generated once, following invocations will only return the same patch.
	 * @throws InvalidClassFileException 
	 */
	public List<OnePatch> GeneratePatch() throws InvalidClassFileException
	{
		ops = new LinkedList<OnePatch>();
		
		if (!overlap)
		{
			// handle this.r
			OnePatch op = new OnePatch(this.r.getSig());
			op.AddLockBeforeIndex(this.r.getBytecodel());
			op.AddUnlockAfterIndex(this.r.getBytecodel());
			ops.add(op);
		}
		
		// handle this.p this.c
		String methodSig = this.p.getSig();
		int pidx = this.p.getBytecodel();
		int cidx = this.c.getBytecodel();
		IR ir = GetMethodIR(methodSig);
		SSACFG cfg = ir.getControlFlowGraph();
		ISSABasicBlock pbk = SearchUtil.GetBasicBlockAccordingToLineNumberInBytecode(pidx, ir);
		ISSABasicBlock cbk = SearchUtil.GetBasicBlockAccordingToLineNumberInBytecode(cidx, ir);
		Set<ISSABasicBlock> pset = new HashSet<ISSABasicBlock>();
		pset.add(pbk);
		pset.add(cbk);
		GetSearchSet(pbk, cfg, true, pset, cbk);
		Set<ISSABasicBlock> cset = new HashSet<ISSABasicBlock>();
		cset.add(pbk);
		cset.add(cbk);
		GetSearchSet(cbk, cfg, false, cset, pbk);
		cset.retainAll(pset);
		Set<ISSABasicBlock> protectnodes = cset;
		OnePatch op = new OnePatch(methodSig);
		ops.add(op);
		
		BasicBlock ent = cfg.entry();
		if (protectnodes.contains(ent))
		{
			op.AddLockBeforeIndex(GetBasicBlockBeforePosition(ent, ir));
		}
		BasicBlock ext = cfg.exit();
		if (protectnodes.contains(ext))
		{
			op.AddUnlockAfterIndex(GetBasicBlockAfterPosition(ext, ir));
		}
		Set<ISSABasicBlock> visited = new HashSet<ISSABasicBlock>();
		visited.add(ent);
		visited.add(ext);
		Set<ISSABasicBlock> blockprelock = new HashSet<ISSABasicBlock>();
		Set<ISSABasicBlock> blockafterunlock = new HashSet<ISSABasicBlock>();
		IterateBlockToAddLockAndUnlock(ent, cfg, protectnodes, visited, blockprelock, blockafterunlock, op, ir);
		return ops;
	}
	
	private void IterateBlockToAddLockAndUnlock(ISSABasicBlock now, SSACFG cfg, Set<ISSABasicBlock> protectnodes, Set<ISSABasicBlock> visited, Set<ISSABasicBlock> blockprelock, Set<ISSABasicBlock> blockafterunlock, OnePatch op, IR ir) throws InvalidClassFileException
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
					op.AddUnlockAfterIndex(GetBasicBlockAfterPosition(now, ir));
					blockafterunlock.add(now);
				}
			}
			if ((!protectnodes.contains(now)) && (protectnodes.contains(ibb)))
			{
				if (!blockprelock.contains(ibb))
				{
					op.AddLockBeforeIndex(GetBasicBlockBeforePosition(ibb, ir));
					blockprelock.add(ibb);
				}
			}
			IterateBlockToAddLockAndUnlock(ibb, cfg, protectnodes, visited, blockprelock, blockafterunlock, op, ir);
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

	private boolean GetSearchSet(ISSABasicBlock nowbk, SSACFG cfg, final boolean forward, Set<ISSABasicBlock> pset, final ISSABasicBlock dest)
	{
		Iterator<ISSABasicBlock> itr = GetBlocks(nowbk, cfg, forward);
		while (itr.hasNext())
		{
			ISSABasicBlock ibb = itr.next();
			if (ibb.equals(dest))
			{
				return true;
			}
			if (pset.contains(ibb))
			{
				return true;
			}
			boolean istodest = GetSearchSet(ibb, cfg, forward, pset, dest);
			if (istodest)
			{
				pset.add(ibb);
			}
		}
		return false;
	}
	
	private Iterator<ISSABasicBlock> GetBlocks(ISSABasicBlock pbk, SSACFG cfg, boolean forward)
	{
		if (forward)
		{
			return cfg.getSuccNodes(pbk);
		}
		else
		{
			return cfg.getPredNodes(pbk);
		}
	}
	
	/**
	 * @param appJar
	 *            should be something like "c:/temp/testdata/java_cup.jar"
	 * @param methodSig
	 *            should be something like "java_cup.lexer.advance()V"
	 * @throws IOException
	 */
	private IR GetMethodIR(String methodSig) {
		MethodReference mr = StringStuff.makeMethodReference(methodSig);
		IMethod m = cha.resolveMethod(mr);
		if (m == null) {
			Assertions.UNREACHABLE("could not resolve " + mr);
		}
		AnalysisOptions options = new AnalysisOptions();
		options.getSSAOptions().setPiNodePolicy(SSAOptions.getAllBuiltInPiNodes());
		AnalysisCache cache = new AnalysisCache();
		IR ir = cache.getSSACache().findOrCreateIR(m, Everywhere.EVERYWHERE, options.getSSAOptions());
		return ir;
	}
	
}
package cn.yyx.labtask.afix.patchgeneration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ibm.wala.cast.java.test.IRTests;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.types.MethodReference;

import cn.yyx.labtask.afix.codemap.SearchUtil;
import cn.yyx.labtask.afix.errordetection.ErrorLocation;
import cn.yyx.labtask.afix.errordetection.ErrorTrace;

public class OnePatchGenerator {

	// IClassHierarchy cha = null;
	CallGraph callGraph = null;

	// String appJar = null;
	ErrorTrace pct = null;
	ErrorTrace rt = null;
	ErrorLocation p = null;
	ErrorLocation c = null;
	ErrorLocation r = null;
	SameLockExclusivePatches ops = null;
	boolean overlap = false;
	int overlapflag = 0; // 0:at the same level. 1:r is lower level. 2:r is
							// upper level and this situation should not be
							// considered.

	// String jar
	public OnePatchGenerator(CallGraph callGraph, ErrorTrace p, ErrorTrace c, ErrorTrace r) throws Exception {
		// this.appJar = jar;
		this.callGraph = callGraph;
		ErrorLocation pel = null;
		ErrorLocation cel = null;
		boolean stop = false;
		Iterator<ErrorLocation> pitr = p.GetNegativeOrderIterator();
		Iterator<ErrorLocation> pitrcl = p.GetNegativeOrderIterator();
		while (pitr.hasNext()) {
			pel = pitr.next();
			pitrcl.next();
			Iterator<ErrorLocation> citr = c.GetNegativeOrderIterator();
			stop = false;
			while (citr.hasNext()) {
				cel = citr.next();
				if (pel.InSameMethod(cel)) {
					stop = true;
					break;
				}
			}
			if (stop) {
				break;
			}
		}
		if (stop) {
			this.p = pel;
			this.c = cel;
			pct = new ErrorTrace();
			while (pitrcl.hasNext()) {
				pct.AddLocationAtNegativeOrder(pitrcl.next());
			}
			// situation 1 : r and p is at same level.
			boolean issitu1 = false;
			Iterator<ErrorLocation> ritr = r.GetNegativeOrderIterator();
			ErrorLocation rel = ritr.next();
			this.r = rel;
			if (pel.InSameMethod(rel)) {
				int pidx = this.p.getLine();
				int cidx = this.c.getLine();
				int ridx = rel.getLine();
				if ((pidx < ridx && ridx < cidx) || (cidx < ridx && ridx < pidx)) {
					overlap = true;
					overlapflag = 0;
					issitu1 = true;
				}
			}
			// situation 2 : r is lower level.
			if (!issitu1) {
				ErrorLocation tel = null;
				while (ritr.hasNext()) {
					tel = ritr.next();
					if (pel.InSameMethod(tel)) {
						int pidx = this.p.getLine();
						int cidx = this.c.getLine();
						int ridx = tel.getLine();
						if ((pidx < ridx && ridx < cidx) || (cidx < ridx && ridx < pidx)) {
							overlap = true;
							overlapflag = 1;
							this.r = tel;
						}
						break;
					}
				}
			}
			rt = new ErrorTrace();
			while (ritr.hasNext()) {
				rt.AddLocationAtNegativeOrder(ritr.next());
			}
			// situation 3 : r is upper level.
			/*
			 * boolean issitu3 = false; if (!issitu1 && !issitu2) { while
			 * (pitr.hasNext()) { ErrorLocation tel = pitr.next(); if
			 * (rel.InSameMethod(tel)) { overlap = true; overlapflag = 2;
			 * issitu3 = true; this.p = tel; this.r = tel; break; } } }
			 */
		} else {
			throw new NoOneScopePatchException("p and c can not put in one (parent) method scope.");
		}
		/*
		 * if (AFixCallGraph.isDirectory(appJar)) { appJar =
		 * AFixCallGraph.findJarFiles(new String[] { appJar }); }
		 */
		// cha = ClassHierarchyManager.GetClassHierarchy(appJar);
		// cha = callGraph.getClassHierarchy();
	}

	/**
	 * 
	 * @return if generated once, following invocations will only return the
	 *         same patch.
	 * @throws InvalidClassFileException
	 */
	public SameLockExclusivePatches GeneratePatch() throws InvalidClassFileException {
		ops = new SameLockExclusivePatches();

		if (!overlap) {
			// handle this.r
			String methodSig = this.r.getSig();
			int ridx = this.r.getLine();
			IR ir = GetMethodIR(methodSig);
			SSACFG cfg = ir.getControlFlowGraph();
			ISSABasicBlock rbk = SearchUtil.GetBasicBlockAccordingToLineNumberInSourcecode(ridx, ir);
			Set<ISSABasicBlock> protectednodes = new HashSet<ISSABasicBlock>();
			protectednodes.add(rbk);
			OnePatch op = new OnePatch(rt, this.r.getSig(), protectednodes, ir, cfg);
			// op.AddLockBeforeIndex(this.r.getBytecodel());
			// op.AddUnlockAfterIndex(this.r.getBytecodel());
			ops.AddPatches(op);
		}

		// handle this.p this.c
		String methodSig = this.p.getSig();
		int pidx = this.p.getLine();
		int cidx = this.c.getLine();
		IR ir = GetMethodIR(methodSig);
		SSACFG cfg = ir.getControlFlowGraph();

		if (methodSig.equals("demo.Example2.main([Ljava/lang/String;)V")) {
			System.out.println("haha haha.");
		}

		ISSABasicBlock pbk = SearchUtil.GetBasicBlockAccordingToLineNumberInSourcecode(pidx, ir);
		ISSABasicBlock cbk = SearchUtil.GetBasicBlockAccordingToLineNumberInSourcecode(cidx, ir);

		if (pbk == null || cbk == null) {
			System.out.println(
					"pmethodSig:" + methodSig + ";cmethodSig:" + this.r.getSig() + ";pbk:" + pbk + ";cbk:" + cbk);
		}

		Set<ISSABasicBlock> pset = new HashSet<ISSABasicBlock>();
		pset.add(pbk);
		pset.add(cbk);
		if (!pbk.equals(cbk)) {
			Set<ISSABasicBlock> visited = new HashSet<ISSABasicBlock>();
			GetSearchSet(pbk, cfg, true, pset, cbk, visited);
		}
		Set<ISSABasicBlock> cset = new HashSet<ISSABasicBlock>();
		cset.add(pbk);
		cset.add(cbk);
		if (!pbk.equals(cbk)) {
			Set<ISSABasicBlock> visited = new HashSet<ISSABasicBlock>();
			GetSearchSet(cbk, cfg, false, cset, pbk, visited);
		}
		cset.retainAll(pset);
		Set<ISSABasicBlock> protectednodes = cset;
		OnePatch op = new OnePatch(pct, methodSig, protectednodes, ir, cfg);
		ops.AddPatches(op);
		return ops;
	}

	private boolean GetSearchSet(ISSABasicBlock nowbk, SSACFG cfg, final boolean forward, Set<ISSABasicBlock> pset,
			final ISSABasicBlock dest, Set<ISSABasicBlock> visited) {
		visited.add(nowbk);
		Iterator<ISSABasicBlock> itr = GetBlocks(nowbk, cfg, forward);
		boolean allres = false;
		while (itr.hasNext()) {
			ISSABasicBlock ibb = itr.next();
			// eliminate self cycle
			if (visited.contains(ibb)) {
				continue;
			}
			if (ibb.equals(dest)) {
				return true;
			}
			if (pset.contains(ibb)) {
				return true;
			}
			boolean istodest = GetSearchSet(ibb, cfg, forward, pset, dest, visited);
			allres = allres || istodest;
			if (istodest) {
				pset.add(ibb);
			}
		}
		return allres;
	}

	private Iterator<ISSABasicBlock> GetBlocks(ISSABasicBlock pbk, SSACFG cfg, boolean forward) {
		if (forward) {
			return cfg.getSuccNodes(pbk);
		} else {
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
		
		// IClassLoader[] loaders = callGraph.getClassHierarchy().getLoaders();
		// for (IClassLoader loader : loaders) {
		//	System.err.println("loader name:" + loader.getName());
		// }
		
		// System.err.println("methodSig:" + methodSig + ";"); // ";appJar:" + appJar + 
		// System.exit(1);
		
		String descriptor = MethodSigToJDTDescriptor(methodSig, "Source"); // "Source#Array1#foo#()V"
		
		// System.err.println("methodSig:" + methodSig + ";" + "descriptor:" + descriptor); // ";appJar:" + appJar + 
		// System.exit(1);
		
		MethodReference mref = IRTests.descriptorToMethodRef(descriptor, callGraph.getClassHierarchy());
		
		// StringBuilder sb = new StringBuilder("");
		// Iterator<CGNode> cgitr = callGraph.iterator();
		// while (cgitr.hasNext())
		// {
		// 	CGNode cgn = cgitr.next();
		// 	sb.append(cgn.toString());
			// System.err.println("CGNode:" + cgn);
		// }
		// try {
		// 	File tf = new File("test_info.txt");
		// 	if (!tf.exists())
		// 	{
		// 		tf.createNewFile();
		// 	}
		// 	FileUtil.ContentToFile(tf, sb.toString());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		// System.exit(1);
		
		CGNode node = callGraph.getNodes(mref).iterator().next();
		
		System.err.println(node);
		System.exit(1);
		
		return node.getIR();

		// MethodReference mr = StringStuff.makeMethodReference(methodSig);
		// IMethod m = cha.resolveMethod(mr);
		// if (m == null) {
		// Assertions.UNREACHABLE("could not resolve " + mr + ";wrong
		// methodsig:" + methodSig);
		// }
		// AnalysisOptions options = new AnalysisOptions();
		// options.getSSAOptions().setPiNodePolicy(SSAOptions.getAllBuiltInPiNodes());
		// AnalysisCache cache = new AnalysisCache();
		// IR ir = cache.getSSACache().findOrCreateIR(m, Everywhere.EVERYWHERE,
		// options.getSSAOptions());
		// return ir;
	}
	
	/**
	 * 
	 * @param methodSig
	 * @param loader. The value must be one of Primordial, Extension, Application, Source and Synthetic.
	 * @return
	 */
	private String MethodSigToJDTDescriptor(String methodSig, String loader)
	{
		methodSig = methodSig.replace('.', '/');
		int msidx = methodSig.indexOf('(');
		String pre = methodSig.substring(0, msidx);
		String post = methodSig.substring(msidx);
		int psidx = pre.lastIndexOf('/');
		String prepre = pre.substring(0, psidx);
		String prepost = pre.substring(psidx+1);
		return loader + "#" + prepre + "#" + prepost + "#" + post;
	}
	
}
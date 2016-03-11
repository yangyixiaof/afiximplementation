package cn.yyx.labtask.afix.patchgeneration;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAOptions;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.io.FileProvider;
import com.ibm.wala.util.strings.StringStuff;

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
	 */
	public List<OnePatch> GeneratePatch()
	{
		ops = new LinkedList<OnePatch>();
		
		if (!overlap)
		{
			OnePatch op = new OnePatch(this.r.getSig());
			op.AddInsertBeforeIndex(this.r.getBytecodel());
			op.AddInsertAfterIndex(this.r.getBytecodel());
			ops.add(op);
		}
		
		IR ir = GetMethodIR(methodSig);
		
		return ops;
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
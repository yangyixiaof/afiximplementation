/*******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cn.yyx.labtask.afix.controlflow;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.examples.drivers.PDFTypeHierarchy;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphStats;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.LocalPointerKey;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.Predicate;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.io.CommandLine;
import com.ibm.wala.util.io.FileProvider;
import com.ibm.wala.util.io.FileUtil;
import com.ibm.wala.util.strings.Atom;

/**
 * This simple example WALA application builds a call graph and fires off
 * ghostview to visualize a DOT representation.
 */
public class AFixCallGraph {
	public static boolean isDirectory(String appJar) {
		return (new File(appJar).isDirectory());
	}

	public static String findJarFiles(String[] directories) throws WalaException {
		Collection<String> result = HashSetFactory.make();
		for (int i = 0; i < directories.length; i++) {
			for (Iterator<File> it = FileUtil.listFiles(directories[i], ".*\\.jar", true).iterator(); it.hasNext();) {
				File f = it.next();
				result.add(f.getAbsolutePath());
			}
		}
		return composeString(result);
	}

	private static String composeString(Collection<String> s) {
		StringBuffer result = new StringBuffer();
		Iterator<String> it = s.iterator();
		for (int i = 0; i < s.size() - 1; i++) {
			result.append(it.next());
			result.append(File.pathSeparator);
		}
		if (it.hasNext()) {
			result.append(it.next());
		}
		return result.toString();
	}

	/**
	 * Usage: args =
	 * "-appJar [jar file name] {-exclusionFile [exclusionFileName]}" The
	 * "jar file name" should be something like "c:/temp/testdata/java_cup.jar"
	 * 
	 * @throws CancelException
	 * @throws IllegalArgumentException
	 */
	public static Process run(String[] args) throws WalaException, IllegalArgumentException, CancelException {
		Properties p = CommandLine.parse(args);
		validateCommandLine(p);
		return run(p.getProperty("appJar"), p.getProperty("exclusionFile", CallGraphTestUtil.REGRESSION_EXCLUSIONS));
	}

	/**
	 * @param appJar
	 *            something like "c:/temp/testdata/java_cup.jar"
	 * @throws CancelException
	 * @throws IllegalArgumentException
	 */
	public static Process run(String appJar, String exclusionFile) throws IllegalArgumentException, CancelException {
		try {
			AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,
					(new FileProvider()).getFile(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
			ClassHierarchy cha = ClassHierarchy.make(scope);
			
			Graph<CGNode> g1 = buildPrunedCallGraph(appJar, (new FileProvider()).getFile(exclusionFile));
			
			Graph<CGNode> g = buildPrunedCallGraph(appJar, (new FileProvider()).getFile(exclusionFile));
			
			System.out.println("g and g1 equals?" + g.equals(g1));
			
			Iterator<CGNode> itr = g.iterator();
			while (itr.hasNext()) {
				CGNode cgn = itr.next();
				Atom mname = cgn.getMethod().getName();
				if (mname.toString().equals("bsearch"))
				{
					System.out.println("Method Found!!!!!");
					IR ir = cgn.getIR();
					
					System.out.print("Print CFG Begin:");
					SSACFG sfg = ir.getControlFlowGraph();
					System.out.print(sfg);
					System.out.print("Print CFG End.\n");
					
					IBytecodeMethod method = (IBytecodeMethod) ir.getMethod();
					SSACFG cfg = ir.getControlFlowGraph();
					BasicBlock bb = cfg.getBasicBlock(4);
					IInstruction[] iis = method.getInstructions();
					for (IInstruction ii : iis)
					{
						System.out.println("ii:" + ii);
					}
					int fidx = bb.getFirstInstructionIndex();
					int lidx = bb.getLastInstructionIndex();
					System.out.println("ii first:"+fidx);
					System.out.println("ii last:"+lidx);
					int bytecodeIndex = method.getBytecodeIndex(10);
					
					System.out.println("bytecodeIndex content:" + iis[12]);
					
					int sourceLineNum = method.getLineNumber(bytecodeIndex);
					System.out.println("bytecodeIndex:"+bytecodeIndex+";sourceIndex:"+sourceLineNum);
					
					System.out.println("======Begin print ir.======");
					SSAInstruction[] iirarr = ir.getInstructions();
					for (int i=0;i<iirarr.length;i++)
					{
						SSAInstruction si = iirarr[i];
						System.out.println("index:" + i + ";sicnt:"+si + ";sidx:" + (si != null ? si.getDef() : "null"));
						// +";siindex:"+si.iindex
					}
					System.out.println("======End print ir.======");
					return PrintUtil.PrintIR(cha, ir);
				}
				System.out.println("Graph Iterate Method:" + mname);
			}
			return PrintUtil.PrintPDF(g);
		} catch (WalaException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidClassFileException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param appJar
	 *            something like "c:/temp/testdata/java_cup.jar"
	 * @return a call graph
	 * @throws CancelException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static Graph<CGNode> buildPrunedCallGraph(String appJar, File exclusionFile)
			throws WalaException, IllegalArgumentException, CancelException, IOException {
		AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,
				exclusionFile != null ? exclusionFile : new File(CallGraphTestUtil.REGRESSION_EXCLUSIONS));

		ClassHierarchy cha = ClassHierarchy.make(scope);

		Iterable<Entrypoint> entrypoints = com.ibm.wala.ipa.callgraph.impl.Util.makeMainEntrypoints(scope, cha);
		AnalysisOptions options = new AnalysisOptions(scope, entrypoints);

		// //
		// build the call graph
		// //
		@SuppressWarnings("rawtypes")
		com.ibm.wala.ipa.callgraph.CallGraphBuilder builder = Util.makeZeroCFABuilder(options, new AnalysisCache(), cha,
				scope);
		CallGraph cg = builder.makeCallGraph(options, null);

		System.err.println(CallGraphStats.getStats(cg));

		Graph<CGNode> g = pruneForAppLoader(cg);

		return g;
	}

	public static Graph<CGNode> pruneForAppLoader(CallGraph g) throws WalaException {
		return PDFTypeHierarchy.pruneGraph(g, new ApplicationLoaderFilter());
	}

	/**
	 * Validate that the command-line arguments obey the expected usage.
	 * 
	 * Usage:
	 * <ul>
	 * <li>args[0] : "-appJar"
	 * <li>args[1] : something like "c:/temp/testdata/java_cup.jar" </ul?
	 * 
	 * @throws UnsupportedOperationException
	 *             if command-line is malformed.
	 */
	public static void validateCommandLine(Properties p) {
		if (p.get("appJar") == null) {
			throw new UnsupportedOperationException("expected command-line to include -appJar");
		}
	}

	/**
	 * A filter that accepts WALA objects that "belong" to the application
	 * loader.
	 * 
	 * Currently supported WALA types include
	 * <ul>
	 * <li>{@link CGNode}
	 * <li>{@link LocalPointerKey}
	 * </ul>
	 */
	private static class ApplicationLoaderFilter extends Predicate<CGNode> {

		@Override
		public boolean test(CGNode o) {
			if (o != null) {
				CGNode n = o;
				return n.getMethod().getDeclaringClass().getClassLoader().getReference()
						.equals(ClassLoaderReference.Application);
			} else if (o instanceof LocalPointerKey) {
				LocalPointerKey l = (LocalPointerKey) o;
				return test(l.getNode());
			} else {
				return false;
			}
		}
	}
}

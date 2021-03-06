package cn.yyx.labtask.afix.controlflow;

import java.io.File;
import java.io.IOException;

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
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.Predicate;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.io.FileProvider;

public class SimpleCallGraph {

	public static Graph<CGNode> GetCallGraph(String appJar) throws IllegalArgumentException, WalaException, CancelException, IOException {
		Graph<CGNode> g = buildPrunedCallGraph(appJar,
				(new FileProvider()).getFile(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
		return g;
	}

	public static Graph<CGNode> buildPrunedCallGraph(String appJar, File exclusionFile)
			throws WalaException, IllegalArgumentException, CancelException, IOException {
		AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,
				exclusionFile != null ? exclusionFile : new File(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
		
		ClassHierarchy cha = ClassHierarchy.make(scope);
		
		Iterable<Entrypoint> entrypoints = com.ibm.wala.ipa.callgraph.impl.Util.makeMainEntrypoints(scope, cha);
		AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
		
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
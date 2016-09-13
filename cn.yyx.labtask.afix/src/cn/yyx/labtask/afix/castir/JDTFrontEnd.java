package cn.yyx.labtask.afix.castir;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import com.ibm.wala.cast.java.client.JDTJavaSourceAnalysisEngine;
import com.ibm.wala.cast.java.ipa.callgraph.JavaSourceAnalysisScope;
import com.ibm.wala.client.AbstractAnalysisEngine;
import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.io.TemporaryFile;

public class JDTFrontEnd {
	
	private CallGraph callGraph = null;
	private IJavaProject project = null;
	private String[] mainClassDescriptors = null;
	
	public JDTFrontEnd(IJavaProject project, String mainClass) {
		this.project = project;
		this.mainClassDescriptors = new String[1];
		this.mainClassDescriptors[0] = mainClass;
		try {
			BuildCallGraph();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (CancelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JDTFrontEnd(IJavaProject project, String[] mainClassDescriptors) {
		this.project = project;
		this.mainClassDescriptors = mainClassDescriptors;
		try {
			BuildCallGraph();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (CancelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private AbstractAnalysisEngine makeAnalysisEngine(final String[] mainClassDescriptors) {
		AbstractAnalysisEngine engine = null;
		try {
			engine = new JDTJavaSourceAnalysisEngine(project) {
				{
					setDump(Boolean.parseBoolean(System.getProperty("wala.cast.dump", "false")));
				}
				@Override
				protected Iterable<Entrypoint> makeDefaultEntrypoints(AnalysisScope scope, IClassHierarchy cha) {
					return Util.makeMainEntrypoints(JavaSourceAnalysisScope.SOURCE, cha, mainClassDescriptors);
				}
			};
			try {
				File tf = TemporaryFile.urlToFile("exclusions.txt",
						CallGraphTestUtil.class.getClassLoader().getResource(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
				engine.setExclusionsFile(tf.getAbsolutePath());
				tf.deleteOnExit();
			} catch (IOException e) {
			}
			return engine;
		} catch (IOException e1) {
			return null;
		} catch (CoreException e1) {
			return null;
		}
	}
	
	private void BuildCallGraph() throws IllegalArgumentException, CancelException, IOException
	{
		@SuppressWarnings("rawtypes")
		AbstractAnalysisEngine engine = makeAnalysisEngine(mainClassDescriptors);
		setCallGraph(engine.buildDefaultCallGraph());
		if (getCallGraph() == null)
		{
			System.err.println("Build call graph into error.");
			System.exit(1);
		}
	}

	public CallGraph getCallGraph() {
		return callGraph;
	}

	public void setCallGraph(CallGraph callGraph) {
		this.callGraph = callGraph;
	}
	
}
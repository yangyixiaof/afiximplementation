package cn.yyx.labtask.afix.castir;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import com.ibm.wala.cast.java.client.JDTJavaSourceAnalysisEngine;
import com.ibm.wala.cast.java.ipa.callgraph.JavaSourceAnalysisScope;
import com.ibm.wala.client.AbstractAnalysisEngine;
import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.util.io.TemporaryFile;

public class JDTFrontEnd {

	IJavaProject project = null;

	public JDTFrontEnd(IJavaProject project) {
		this.project = project;
	}

	protected AbstractAnalysisEngine getAnalysisEngine(final String[] mainClassDescriptors, Collection<String> sources,
			List<String> libs) {
		return makeAnalysisEngine(mainClassDescriptors, sources, libs);
	}

	private AbstractAnalysisEngine makeAnalysisEngine(final String[] mainClassDescriptors, Collection<String> sources,
			List<String> libs) {
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

}
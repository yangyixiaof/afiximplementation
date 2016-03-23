package cn.yyx.labtask.afix.patchgeneration;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.io.FileProvider;

public class ClassHierarchyManager {
	
	private static Map<String, ClassHierarchy> chamap = new TreeMap<String, ClassHierarchy>();
	
	public static ClassHierarchy GetClassHierarchy(String appJar) throws IOException, ClassHierarchyException {
		ClassHierarchy cha = chamap.get(appJar);
		if (cha == null)
		{
			AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,
					(new FileProvider()).getFile(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
			cha = ClassHierarchy.make(scope);
			chamap.put(appJar, cha);
		}
		return cha;
	}
	
	public static void Clear()
	{
		chamap.clear();
	}
	
}
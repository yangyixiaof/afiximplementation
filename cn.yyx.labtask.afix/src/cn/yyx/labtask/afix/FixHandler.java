package cn.yyx.labtask.afix;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;

import com.ibm.wala.ipa.callgraph.CallGraph;

import cn.yyx.labtask.afix.castir.JDTFrontEnd;
import cn.yyx.labtask.afix.classmodification.SourceFileModifier;
import cn.yyx.labtask.afix.commonutil.FileUtil;
import cn.yyx.labtask.afix.commonutil.NameUtil;
import cn.yyx.labtask.afix.errordetection.ErrorTrace;
import cn.yyx.labtask.afix.errordetection.OneErrorInfo;
import cn.yyx.labtask.afix.ideutil.EclipseHelper;
import cn.yyx.labtask.afix.patchgeneration.ClassHierarchyManager;
import cn.yyx.labtask.afix.patchgeneration.ExclusivePatchesManager;
import cn.yyx.labtask.afix.patchgeneration.OnePatchGenerator;
import cn.yyx.labtask.afix.patchgeneration.SameLockExclusivePatches;
import cn.yyx.labtask.afix.raceinput.PCRPool;
import cn.yyx.labtask.afix.raceinput.RaceReportHandler;

public class FixHandler {
	
	IJavaProject ijp = null;
	
	public FixHandler(IJavaProject ijp) {
		this.ijp = ijp;
	}
	
	// String inputjar, String outputjar, , String projectname
	private void HandleTraces(List<OneErrorInfo> oeilist, CallGraph callGraph) throws Exception {
		ExclusivePatchesManager epm = new ExclusivePatchesManager();
		Iterator<OneErrorInfo> itr = oeilist.iterator();
		while (itr.hasNext())
		{
			OneErrorInfo oei = itr.next();
			ErrorTrace p = oei.getP();
			ErrorTrace c = oei.getC();
			ErrorTrace r = oei.getR();
			// inputjar
			OnePatchGenerator opg = new OnePatchGenerator(callGraph, p, c, r);
			SameLockExclusivePatches sp = opg.GeneratePatch();
			epm.AddOneExclusivePatch(sp);
		}
		epm.MergeSelf();
		// JarModifier jm = new JarModifier(inputjar, outputjar);
		// jm.HandleExclusivePatchesManager(epm);
		SourceFileModifier sfm = new SourceFileModifier(ijp); // projectname
		sfm.HandleExclusivePatchesManager(epm);
	}
	
	public static void HandleRaceReport(String reportcontent, String javaprojectname, String mainclass)
	{
		File repf = new File("tempreport");
		try {
			FileUtil.ContentToFile(repf, reportcontent);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		HandleRaceReport(repf, javaprojectname, mainclass);
	}
	
	public static void HandleRaceReport(File reportfile, String javaprojectname, String mainclass)
	{
		mainclass = "L" + NameUtil.TranslateJVMNameToUnifiedForm(mainclass);
		PCRPool pcr = null;
		try {
			// new File("RaceReport/report")
			pcr = RaceReportHandler.ReadReport(reportfile);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		IJavaProject ijp = EclipseHelper.GetSpecifiedProject(javaprojectname);
		JDTFrontEnd jdtfe = new JDTFrontEnd(ijp, mainclass);
		CallGraph jdtcg = jdtfe.getCallGraph();
		
		// String inputjar = "TestInputJar/Example4.jar";
		// String outputjar = "TestOutputJar/Example4.jar";
		// String projectname = "SourceDir";
		
		FixHandler fh = new FixHandler(ijp);
		List<OneErrorInfo> oeilist = pcr.GetTraces(); // para: inputjar.
		try {
			// fh.HandleTraces(oeilist, inputjar, outputjar, projectname);
			fh.HandleTraces(oeilist, jdtcg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ClassHierarchyManager.Clear();
	}
	
	/*public static void main(String[] args) {
		{
			// example 1.
			FixHandler fh = new FixHandler();
			ErrorTrace p = new ErrorTrace();
			p.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example$MyThread.run()V", 0));
			ErrorTrace c = new ErrorTrace();
			c.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example$MyThread.run()V", 29));
			ErrorTrace r = new ErrorTrace();
			r.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example.main([Ljava/lang/String;)V", 27));
			List<OneErrorInfo> oeilist = new LinkedList<OneErrorInfo>();
			oeilist.add(new OneErrorInfo(p, c, r));
			String inputjar = "TestInputJar/Example.jar";
			String outputjar = "TestOutputJar/Example.jar";
			String projectname = "SourceDir";
			try {
				fh.HandleTraces(oeilist, inputjar, outputjar, projectname);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		{
			// example 2.
			FixHandler fh = new FixHandler();
			List<OneErrorInfo> oeilist = new LinkedList<OneErrorInfo>();
			{
				ErrorTrace p = new ErrorTrace();
				p.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example2.main([Ljava/lang/String;)V", 24));
				ErrorTrace c = new ErrorTrace();
				c.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example2.main([Ljava/lang/String;)V", 66));
				ErrorTrace r = new ErrorTrace();
				r.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example2$MyThread.run()V", 0));
				oeilist.add(new OneErrorInfo(p, c, r));
			}
			{
				ErrorTrace p = new ErrorTrace();
				p.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example2$MyThread.run()V", 0));
				ErrorTrace c = new ErrorTrace();
				c.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example2$MyThread.run()V", 29));
				ErrorTrace r = new ErrorTrace();
				r.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example2.main([Ljava/lang/String;)V", 24));
				oeilist.add(new OneErrorInfo(p, c, r));
			}
			String inputjar = "TestInputJar/Example2.jar";
			String outputjar = "TestOutputJar/Example2.jar";
			String projectname = "SourceDir";
			try {
				fh.HandleTraces(oeilist, inputjar, outputjar, projectname);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
}
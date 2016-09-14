package cn.yyx.labtask.afix;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
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
	private void HandleTraces(List<OneErrorInfo> oeilist, CallGraph callGraph, IProgressMonitor monitor) throws Exception {
		ExclusivePatchesManager epm = new ExclusivePatchesManager();
		
		int totalidx = oeilist.size();
		System.err.println("OneErrorInfo List size:" + totalidx);
		// System.exit(1);
		
		if (totalidx == 0)
		{
			monitor.subTask("No race, waiting to exit: 3...");
			monitor.worked(30);
			
			Thread.sleep(1000);
			
			monitor.subTask("No race, waiting to exit: 2...");
			monitor.worked(30);
			
			Thread.sleep(500);
			
			monitor.subTask("No race, waiting to exit: 1...");
			monitor.worked(30);
			
			Thread.sleep(500);
			return;
		}
		
		// 90% work here.
		monitor.subTask("Start generating all patches, all size:" + totalidx + ".");
		if (monitor.isCanceled())
		{
			return;
		}
		int idx = 0;
		int temptotal = 0;
		int avesize = 30/totalidx;
		Iterator<OneErrorInfo> itr = oeilist.iterator();
		while (itr.hasNext())
		{
			idx++;
			
			OneErrorInfo oei = itr.next();
			ErrorTrace p = oei.getP();
			ErrorTrace c = oei.getC();
			ErrorTrace r = oei.getR();
			// inputjar
			
			temptotal += avesize;
			
			monitor.subTask("Generate the patch:" + idx + "/" + totalidx + ".");
			
			OnePatchGenerator opg = new OnePatchGenerator(callGraph, p, c, r);
			SameLockExclusivePatches sp = opg.GeneratePatch();
			epm.AddOneExclusivePatch(sp);
			
			if (monitor.isCanceled())
			{
				return;
			}
			monitor.worked(avesize);
		}
		if (monitor.isCanceled())
		{
			return;
		}
		monitor.worked(30 - temptotal);
		
		monitor.subTask("Merge all patches......");
		epm.MergeSelf();
		if (monitor.isCanceled())
		{
			return;
		}
		monitor.worked(30);
		
		// JarModifier jm = new JarModifier(inputjar, outputjar);
		// jm.HandleExclusivePatchesManager(epm);
		monitor.subTask("Modify all source codes.");
		if (monitor.isCanceled())
		{
			return;
		}
		SourceFileModifier sfm = new SourceFileModifier(ijp); // projectname
		sfm.HandleExclusivePatchesManager(epm);
		monitor.worked(30);
	}
	
	public static void HandleRaceReport(String reportcontent, String javaprojectname, String mainclass, IProgressMonitor monitor)
	{
		File repf = new File("tempreport");
		try {
			FileUtil.ContentToFile(repf, reportcontent);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		HandleRaceReport(repf, javaprojectname, mainclass, monitor);
	}
	
	public static void HandleRaceReport(File reportfile, String javaprojectname, String mainclass, IProgressMonitor monitor)
	{
		monitor.subTask("Transform main class name.");
		mainclass = "L" + NameUtil.TranslateJVMNameToUnifiedForm(mainclass);
		if (monitor.isCanceled())
		{
			return;
		}
		monitor.worked(1);
		
		monitor.subTask("Read race report.");
		PCRPool pcr = null;
		try {
			// new File("RaceReport/report")
			pcr = RaceReportHandler.ReadReport(reportfile);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if (monitor.isCanceled())
		{
			return;
		}
		monitor.worked(3);
		
		monitor.subTask("Find suitable project.");
		IJavaProject ijp = EclipseHelper.GetSpecifiedProject(javaprojectname);
		if (monitor.isCanceled())
		{
			return;
		}
		monitor.worked(2);
		
		monitor.subTask("Build call graph.");
		JDTFrontEnd jdtfe = null;
		try {
			jdtfe = new JDTFrontEnd(ijp, mainclass);
		} catch (Exception e) {
			System.err.println("ijp:" + ijp + ";mainclass:" + mainclass);
			throw e;
		}
		CallGraph jdtcg = jdtfe.getCallGraph();
		if (monitor.isCanceled())
		{
			return;
		}
		monitor.worked(3);
		
		// printing.
		/*try {
			Iterator<CGNode> itr = jdtcg.iterator();
			itr.next();
			CGNode cgn = itr.next();
			PrintUtil.PrintIR(cgn.getClassHierarchy(), cgn.getIR());
		} catch (WalaException e1) {
			e1.printStackTrace();
		}
		System.exit(1);*/
		
		// String inputjar = "TestInputJar/Example4.jar";
		// String outputjar = "TestOutputJar/Example4.jar";
		// String projectname = "SourceDir";
		
		monitor.subTask("Get race traces.");
		FixHandler fh = new FixHandler(ijp);
		List<OneErrorInfo> oeilist = pcr.GetTraces(); // para: inputjar.
		if (monitor.isCanceled())
		{
			return;
		}
		monitor.worked(1);
		
		try {
			// fh.HandleTraces(oeilist, inputjar, outputjar, projectname);
			fh.HandleTraces(oeilist, jdtcg, monitor);
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
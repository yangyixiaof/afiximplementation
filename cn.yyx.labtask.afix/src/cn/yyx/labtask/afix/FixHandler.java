package cn.yyx.labtask.afix;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.labtask.afix.classmodification.JarModifier;
import cn.yyx.labtask.afix.errordetection.ErrorLocation;
import cn.yyx.labtask.afix.errordetection.ErrorTrace;
import cn.yyx.labtask.afix.errordetection.OneErrorInfo;
import cn.yyx.labtask.afix.patchgeneration.ExclusivePatchesManager;
import cn.yyx.labtask.afix.patchgeneration.OnePatchGenerator;
import cn.yyx.labtask.afix.patchgeneration.SameLockExclusivePatches;

public class FixHandler {
	
	public FixHandler() {
		
	}
	
	private void HandleTraces(List<OneErrorInfo> oeilist, String inputjar, String outputjar) throws Exception {
		ExclusivePatchesManager epm = new ExclusivePatchesManager();
		Iterator<OneErrorInfo> itr = oeilist.iterator();
		while (itr.hasNext())
		{
			OneErrorInfo oei = itr.next();
			ErrorTrace p = oei.getP();
			ErrorTrace c = oei.getC();
			ErrorTrace r = oei.getR();
			OnePatchGenerator opg = new OnePatchGenerator(inputjar, p, c, r);
			SameLockExclusivePatches sp = opg.GeneratePatch();
			epm.AddOneExclusivePatch(sp);
		}
		epm.MergeSelf();
		JarModifier jm = new JarModifier(inputjar, outputjar);
		jm.HandleExclusivePatchesManager(epm);
	}

	public static void main(String[] args) {
		FixHandler fh = new FixHandler();
		ErrorTrace p = new ErrorTrace();
		p.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example$MyThread.run()V", 0));
		ErrorTrace c = new ErrorTrace();
		c.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example$MyThread.run()V", 26));
		ErrorTrace r = new ErrorTrace();
		r.AddLocationAtPositiveOrder(new ErrorLocation("demo.Example.main([Ljava/lang/String;)V", 27));
		List<OneErrorInfo> oeilist = new LinkedList<OneErrorInfo>();
		oeilist.add(new OneErrorInfo(p, c, r));
		String inputjar = "TestInputJar/demo.jar";
		String outputjar = "TestOutputJar/demo.jar";
		try {
			fh.HandleTraces(oeilist, inputjar, outputjar);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
package cn.yyx.labtask.afix.raceinput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cn.yyx.labtask.afix.rvparse.RVStructureVisitor;

public class RaceReportHandler {
	
	public static PCRPool ReadReport(File f) throws IOException
	{
		PCRPool pp = new PCRPool();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String online = null;
		while ((online = br.readLine()) != null)
		{
			if (!online.startsWith("Race:"))
			{
				continue;
			}
			RVStructureVisitor evalVisitor = new RVStructureVisitor(pp);
			try {
				ParseRoot.ParseOneSentence(online, evalVisitor, false);
			} catch (Exception e) {
				e.printStackTrace();
				// testing
				System.err.println("TEST: Wrong parse sentence:" + online);
			}
		}
		br.close();
		return pp;
	}
	
}
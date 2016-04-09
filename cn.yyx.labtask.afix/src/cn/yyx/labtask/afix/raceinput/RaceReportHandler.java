package cn.yyx.labtask.afix.raceinput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RaceReportHandler {
	
	public static PCRPool ReadReport(File f) throws IOException
	{
		PCRPool pp = new PCRPool();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String online = null;
		while ((online = br.readLine()) != null)
		{
			
		}
		br.close();
		return pp;
	}
	
}
package cn.yyx.labtask.afix.handlers;

import java.io.File;

public class HandlerFileTask extends HandlerTask {
	
	private File report_file = null;
	
	public HandlerFileTask(File report_file, String projectname, String mainclass) {
		super(projectname, mainclass);
		this.setReport_file(report_file);
	}
	
	public File getReport_file() {
		return report_file;
	}
	
	public void setReport_file(File report_file) {
		this.report_file = report_file;
	}
	
}
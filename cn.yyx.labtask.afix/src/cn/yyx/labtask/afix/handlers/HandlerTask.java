package cn.yyx.labtask.afix.handlers;

public abstract class HandlerTask implements Comparable<HandlerTask> {
	
	private String projectname = null;
	private String mainclass = null;
	
	public HandlerTask(String projectname, String mainclass) {
		this.setProjectname(projectname);
		this.setMainclass(mainclass);
	}
	
	@Override
	public int compareTo(HandlerTask o) {
		return projectname.compareTo(o.projectname);
	}
	
	public String getProjectname() {
		return projectname;
	}
	
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	
	public String getMainclass() {
		return mainclass;
	}
	
	public void setMainclass(String mainclass) {
		this.mainclass = mainclass;
	}
	
	@Override
	public String toString() {
		return mainclass;
	}
	
}
package cn.yyx.labtask.afix.handlers;

public class HandlerStringTask extends HandlerTask {
	
	private String content = null;
	
	public HandlerStringTask(String content, String projectname, String mainclass) {
		super(projectname, mainclass);
		this.setContent(content);
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
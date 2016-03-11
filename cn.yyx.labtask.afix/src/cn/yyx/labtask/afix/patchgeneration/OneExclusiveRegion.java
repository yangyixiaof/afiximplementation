package cn.yyx.labtask.afix.patchgeneration;

public class OneExclusiveRegion {
	
	private String rightregionsig = null;
	private int rightinsertbeginindex = -1;
	private int rightinsertendindex = -1;
	
	public OneExclusiveRegion(String rightregionsig, int rightinsertbeginindex, int rightinsertendindex) {
		this.setRightregionsig(rightregionsig);
		this.setRightinsertbeginindex(rightinsertbeginindex);
		this.setRightinsertendindex(rightinsertendindex);
	}

	public String getRightregionsig() {
		return rightregionsig;
	}

	public void setRightregionsig(String rightregionsig) {
		this.rightregionsig = rightregionsig;
	}

	public int getRightinsertbeginindex() {
		return rightinsertbeginindex;
	}

	public void setRightinsertbeginindex(int rightinsertbeginindex) {
		this.rightinsertbeginindex = rightinsertbeginindex;
	}

	public int getRightinsertendindex() {
		return rightinsertendindex;
	}

	public void setRightinsertendindex(int rightinsertendindex) {
		this.rightinsertendindex = rightinsertendindex;
	}
	
}

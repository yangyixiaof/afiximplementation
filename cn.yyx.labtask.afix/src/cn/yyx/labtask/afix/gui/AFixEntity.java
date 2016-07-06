package cn.yyx.labtask.afix.gui;

public class AFixEntity {
	
	private String lockname = null;
	private String operationtype = null;
	private String locklocation = null;
	
	public AFixEntity(String lockname, String operationtype, String locklocation) {
		this.setLockname(lockname);
		this.setOperationtype(operationtype);
		this.setLocklocation(locklocation);
	}

	public String getLockname() {
		return lockname;
	}

	public void setLockname(String lockname) {
		this.lockname = lockname;
	}

	public String getLocklocation() {
		return locklocation;
	}

	public void setLocklocation(String locklocation) {
		this.locklocation = locklocation;
	}

	public String getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}
	
}
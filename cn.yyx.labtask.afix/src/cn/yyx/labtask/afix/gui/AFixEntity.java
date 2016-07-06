package cn.yyx.labtask.afix.gui;

public class AFixEntity {
	
	private String lockname = null;
	private String operationtype = null;
	private String locklocation = null;
	private String lockfullnamelocation = null;
	
	public AFixEntity(String lockname, String operationtype, String locklocation, String lockfullnamelocation) {
		this.setLockname(lockname);
		this.setOperationtype(operationtype);
		this.setLocklocation(locklocation);
		this.setLockfullnamelocation(lockfullnamelocation);
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

	public String getLockfullnamelocation() {
		return lockfullnamelocation;
	}

	public void setLockfullnamelocation(String lockfullnamelocation) {
		this.lockfullnamelocation = lockfullnamelocation;
	}
	
}
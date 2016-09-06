package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.Statement;

public class StatementSort implements Comparable<StatementSort> {
	
	private Statement stmt = null;
	
	public StatementSort(Statement stmt) {
		this.setStatement(stmt);
	}
	
	@Override
	public int compareTo(StatementSort o) {
		return new Integer(getStatement().getStartPosition()).compareTo(new Integer(o.getStatement().getStartPosition()));
	}

	public Statement getStatement() {
		return stmt;
	}

	public void setStatement(Statement stmt) {
		this.stmt = stmt;
	}
	
}
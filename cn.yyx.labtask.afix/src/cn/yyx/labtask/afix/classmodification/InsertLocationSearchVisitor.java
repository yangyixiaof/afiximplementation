package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class InsertLocationSearchVisitor extends ASTVisitor{
	
	private ASTNode insertnode = null;
	
	int sourceline = -1;
	
	public InsertLocationSearchVisitor(int sourceline)
	{
		this.sourceline = sourceline;
	}

	public ASTNode getInsertnode() {
		return insertnode;
	}

	public void setInsertnode(ASTNode insertnode) {
		this.insertnode = insertnode;
	}
	
}
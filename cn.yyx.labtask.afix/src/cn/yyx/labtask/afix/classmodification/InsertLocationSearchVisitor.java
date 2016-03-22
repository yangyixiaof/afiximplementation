package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class InsertLocationSearchVisitor extends ASTVisitor{
	
	private ASTNode insertnode = null;
	
	int offsetfrombegining = -1;
	
	boolean before = false;
	
	public InsertLocationSearchVisitor(int offsetfrombegining, boolean before)
	{
		this.offsetfrombegining = offsetfrombegining;
		this.before = before;
	}
	
	@Override
	public void preVisit(ASTNode node) {
		node.
		super.preVisit(node);
	}

	public ASTNode getInsertnode() {
		return insertnode;
	}

	public void setInsertnode(ASTNode insertnode) {
		this.insertnode = insertnode;
	}
	
}
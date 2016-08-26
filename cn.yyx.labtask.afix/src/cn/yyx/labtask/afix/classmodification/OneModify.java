package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

public class OneModify {
	
	private ListRewrite listRewrite = null;
	private AST ast = null;
	private MethodInvocation newInvocation = null;
	private ASTNode insertnode = null;
	private boolean isinsertbefore = false;
	
	public OneModify(ListRewrite listRewrite, AST ast, MethodInvocation newInvocation, ASTNode insertnode, boolean isinsertbefore) {
		this.setListRewrite(listRewrite);
		this.setAst(ast);
		this.setNewStatement(newInvocation);
		this.setInsertnode(insertnode);
		this.setIsinsertbefore(isinsertbefore);
	}

	public ListRewrite getListRewrite() {
		return listRewrite;
	}

	public void setListRewrite(ListRewrite listRewrite) {
		this.listRewrite = listRewrite;
	}

	public MethodInvocation getNewStatement() {
		return newInvocation;
	}

	public void setNewStatement(MethodInvocation newStatement) {
		this.newInvocation = newStatement;
	}

	public ASTNode getInsertnode() {
		return insertnode;
	}

	public void setInsertnode(ASTNode insertnode) {
		this.insertnode = insertnode;
	}

	public boolean isIsinsertbefore() {
		return isinsertbefore;
	}

	public void setIsinsertbefore(boolean isinsertbefore) {
		this.isinsertbefore = isinsertbefore;
	}

	public AST getAst() {
		return ast;
	}

	public void setAst(AST ast) {
		this.ast = ast;
	}
	
}
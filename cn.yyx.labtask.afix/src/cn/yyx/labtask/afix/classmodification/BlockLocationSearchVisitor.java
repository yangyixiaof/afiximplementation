package cn.yyx.labtask.afix.classmodification;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.yyx.labtask.afix.commonutil.SearchOrder;

public class BlockLocationSearchVisitor extends ASTVisitor{
	
	SearchOrder so = null;
	private Block result = null;
	
	public BlockLocationSearchVisitor(SearchOrder so) {
		this.so = so;
	}

	public Block getResult() {
		return result;
	}

	public void setResult(Block result) {
		this.result = result;
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		boolean ctn = so.HandleCurrentClass(node);
		return ctn && super.visit(node);
	}
	
	@Override
	public void endVisit(TypeDeclaration node) {
		so.DeHandleCurrentClass(node);
		super.endVisit(node);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(MethodDeclaration node) {
		boolean ctn = true;
		if (so.IsInRightClass())
		{
			Type tp = node.getReturnType2();
			List<SingleVariableDeclaration> params = node.parameters();
			if (so.IsInRightMethod(tp, params))
			{
				result = node.getBody();
			}
			ctn = false;
		}
		return ctn && super.visit(node);
	}
	
}
package cn.yyx.labtask.afix.classmodification;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ASTHelper {

	public static int GetASTNodeLineNumber(CompilationUnit compilationUnit, ASTNode astnode) {
		int linenumber = compilationUnit.getLineNumber(compilationUnit.getExtendedStartPosition(astnode));
		return linenumber;
	}

	public static List<Integer> GetLockASTNodeLineNumber(CompilationUnit compilationUnit, String lockname) {
		List<Integer> ils = new LinkedList<Integer>();
		compilationUnit.accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodInvocation node) {
				if (node.getName().toString().equals("lock"))
				{
					String nexs = node.getExpression().toString();
					if (nexs.equals("cn.yyx.labtask.afix.LockPool." + lockname))
					{
						ils.add(GetASTNodeLineNumber(compilationUnit, node));
					}
				}
				return super.visit(node);
			}
		});
		return ils;
	}

	public static List<Integer> GetUnLockASTNodeLineNumber(CompilationUnit compilationUnit, String lockname) {
		List<Integer> ils = new LinkedList<Integer>();
		compilationUnit.accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodInvocation node) {
				if (node.getName().toString().equals("unlock"))
				{
					String nexs = node.getExpression().toString();
					if (nexs.equals("cn.yyx.labtask.afix.LockPool." + lockname))
					{
						ils.add(GetASTNodeLineNumber(compilationUnit, node));
					}
				}
				return super.visit(node);
			}
		});
		return ils;
	}

}
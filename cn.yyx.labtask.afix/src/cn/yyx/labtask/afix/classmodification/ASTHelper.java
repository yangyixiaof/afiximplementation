package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTHelper {
	
	public static int GetASTNodeLineNumber(CompilationUnit compilationUnit,ASTNode astnode)
	{
		int linenumber = compilationUnit.getLineNumber(compilationUnit.getExtendedStartPosition(astnode));
		return linenumber;
	}
	
}
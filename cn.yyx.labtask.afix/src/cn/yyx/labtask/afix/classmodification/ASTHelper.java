package cn.yyx.labtask.afix.classmodification;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jface.text.Document;

import cn.yyx.labtask.afix.commonutil.FileUtil;

public class ASTHelper {

	public static int GetASTNodeLineNumber(CompilationUnit compilationUnit, ASTNode astnode) {
		int linenumber = compilationUnit.getLineNumber(compilationUnit.getExtendedStartPosition(astnode));
		return linenumber;
	}

	public static List<Integer> GetLockASTNodeLineNumber(CompilationUnit compilationUnit, String lockname) {
		List<Integer> ils = new LinkedList<Integer>();
		ils.clear();
		compilationUnit.accept(new ASTVisitor() {
			@Override
			public boolean visit(SynchronizedStatement node) {
				if (node.getExpression().toString().trim().equals("cn.yyx.labtask.afix.LockPool."+lockname))
				{
					ils.add(GetASTNodeLineNumber(compilationUnit, node));
				}
				return false;
			}
		});
		return ils;
	}

	public static List<Integer> GetUnLockASTNodeLineNumber(CompilationUnit compilationUnit, String lockname) {
		List<Integer> ils = new LinkedList<Integer>();
		// compilationUnit.accept(new ASTVisitor() {
		//	@Override
		//	public boolean visit(MethodInvocation node) {
		//		if (node.getName().toString().equals("unlock"))
		//		{
		//			String nexs = node.getExpression().toString();
		//			if (nexs.equals("cn.yyx.labtask.afix.LockPool." + lockname))
		//			{
		//				ils.add(GetASTNodeLineNumber(compilationUnit, node));
		//			}
		//		}
		//		return super.visit(node);
		//	}
		// });
		return ils;
	}
	
	public static CompilationUnit GetCompilationUnit(File df) {
		Document document = new Document(FileUtil.ReadFileByLines(df));
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(document.get().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}
	
}
package cn.yyx.labtask.afix.irengine;

import java.io.File;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import cn.yyx.labtask.afix.classmodification.ASTHelper;
import cn.yyx.labtask.afix.commonutil.FileUtil;

public class Test {
	
	public void heihei()
	{
		int x = 0;
		{
			if (x == 1)
			{
				x = 6;
			} else {
				x = 5;
			}
			System.err.println(x);
		}
		
		java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(Integer.MAX_VALUE);
		System.err.println(semaphore);
	}
	
	public static void main(String[] args) {
		File f = new File("SourceBackDir/IRGenerator.java");
		CompilationUnit cu = ASTHelper.GetCompilationUnit(f);
		AST ast = cu.getAST();
		ASTRewrite aw = ASTRewrite.create(ast);
		TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
		MethodDeclaration md = td.getMethods()[0];
		Block body = md.getBody();
		Statement stmt = (Statement) body.statements().get(0);
		SynchronizedStatement newsyn = ast.newSynchronizedStatement();
		newsyn.setExpression(ast.newSimpleName("haha"));
		ListRewrite listRewrite = aw.getListRewrite(body, Block.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(newsyn, stmt, null);
		Document document = new Document(FileUtil.ReadFileByLines(f));
		TextEdit edits = aw.rewriteAST(document, null);
		FileUtil.ClearAndWriteToFile(document.get(), f);
		try {
			edits.apply(document);
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		// System.err.println("total length:" + cu.getLength() + ";57,1 position:" + cu.getPosition(57, 1) + ";1,55 position:" + cu.getPosition(1, 55) + ";line:" + cu.getLineNumber(0) + ";position:" + cu.getPosition(3, 0) + ";col:" + cu.getColumnNumber(37));
	}
	
}
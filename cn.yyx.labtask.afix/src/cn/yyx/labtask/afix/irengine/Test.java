package cn.yyx.labtask.afix.irengine;

import java.io.File;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TextElement;
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
		File f = new File("SourceBackDir/Ha.java");
		CompilationUnit cu = ASTHelper.GetCompilationUnit(f);
		
		cu.accept(new ASTVisitor() {
			@Override
			public void preVisit(ASTNode node) {
				System.out.println("node cls:" + node.getClass() + ";node cnt:" + node.toString());
				super.preVisit(node);
			}
		});
		
		AST ast = cu.getAST();
		ASTRewrite aw = ASTRewrite.create(ast);
		TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
		MethodDeclaration md = td.getMethods()[0];
		Block body = md.getBody();
		Statement stmt = (Statement) body.statements().get(0);
		
		TextElement siso1 = ast.newTextElement();
		siso1.setText("System.out.println(\"hello1\");");
		TextElement siso2 = ast.newTextElement();
		siso2.setText("System.out.println(\"hello2\");");
		
		ListRewrite listRewrite = aw.getListRewrite(body, Block.STATEMENTS_PROPERTY);
		
		listRewrite.remove(stmt, null);
		
		listRewrite.insertBefore(siso1, stmt, null);
		listRewrite.insertBefore(siso2, stmt, null);
		
		SynchronizedStatement newsyn = ast.newSynchronizedStatement();
		newsyn.setExpression(ast.newSimpleName("haha"));
		Block bk = newsyn.getBody();
		ListRewrite bkListRewrite = aw.getListRewrite(bk, Block.STATEMENTS_PROPERTY);
		bkListRewrite.insertLast(stmt, null);
		
		listRewrite.insertBefore(newsyn, stmt, null);
		
		Document document = new Document(FileUtil.ReadFileByLines(f));
		TextEdit edits = aw.rewriteAST(document, null);
		try {
			edits.apply(document);
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		try {
			FileUtil.ContentToFile(f, document.get());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// System.err.println("total length:" + cu.getLength() + ";57,1 position:" + cu.getPosition(57, 1) + ";1,55 position:" + cu.getPosition(1, 55) + ";line:" + cu.getLineNumber(0) + ";position:" + cu.getPosition(3, 0) + ";col:" + cu.getColumnNumber(37));
	}
	
}
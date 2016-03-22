package cn.yyx.labtask.afix.classmodification;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import com.ibm.wala.shrikeCT.InvalidClassFileException;

import cn.yyx.labtask.afix.commonutil.FileUtil;
import cn.yyx.labtask.afix.commonutil.NameUtil;
import cn.yyx.labtask.afix.commonutil.SearchOrder;
import cn.yyx.labtask.afix.patchgeneration.ExclusivePatchesManager;
import cn.yyx.labtask.afix.patchgeneration.OnePatch;
import cn.yyx.labtask.afix.patchgeneration.SameLockExclusivePatches;

public class SourceFileModifier {
	
	String sourcefolder = null;
	Map<String, File> allfiles = new TreeMap<String, File>();
	Map<String, File> exactmatchfile = new TreeMap<String, File>();
	Map<String, String> filecontent = new TreeMap<String, String>();
	Map<String, ASTRewrite> allrewrites = new TreeMap<String, ASTRewrite>();
	Map<String, CompilationUnit> cus = new TreeMap<String, CompilationUnit>();
	
	public SourceFileModifier(String sourcefolder) {
		FileUtil.GetAllFilesInADirectory(new File(sourcefolder), allfiles);
	}
	
	public void HandleExclusivePatchesManager(ExclusivePatchesManager epm) throws InvalidClassFileException, JavaModelException, IllegalArgumentException, MalformedTreeException, BadLocationException
	{
		Iterator<SameLockExclusivePatches> itr = epm.Iterator();
		int lockidx = 0;
		while (itr.hasNext()) {
			lockidx++;
			String lockname = "lock" + lockidx;
			SameLockExclusivePatches slep = itr.next();
			Iterator<OnePatch> opitr = slep.GetIterator();
			while (opitr.hasNext()) {
				OnePatch op = opitr.next();
				String msig = op.getMethodsig();
				String mtype = NameUtil.GetClassNameFromMethodSig(msig);
				CompilationUnit cu = GetCompilationUnit(msig);
				AST ast = cu.getAST();
				ASTRewrite aw = GetASTRewriteAccordingToMethodSig(msig, ast);
				SearchOrder so = new SearchOrder(msig);
				BlockLocationSearchVisitor blvisitor = new BlockLocationSearchVisitor(so);
				cu.accept(blvisitor);
				Block methodblock = blvisitor.getResult();
				ListRewrite listRewrite = aw.getListRewrite(methodblock, Block.STATEMENTS_PROPERTY);
				
				{
					Iterator<Integer> sbitr = op.GetInsertPosBeginSourceIterator();
					while (sbitr.hasNext())
					{
						int posline = sbitr.next();
						int poslineoff =  FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(), posline-1, GetFileContent(mtype));
						InsertLocationSearchVisitor ilsv = new InsertLocationSearchVisitor(poslineoff, false);
						methodblock.accept(ilsv);
						ASTNode insertnode = ilsv.getInsertnode();
						MethodInvocation newInvocation = ast.newMethodInvocation();
						newInvocation.setName(ast.newSimpleName("lock"));
						newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool."+lockname));
						Statement newStatement = ast.newExpressionStatement(newInvocation);
						listRewrite.insertAfter(insertnode, newStatement, null);
					}
				}
				
				{
					Iterator<Integer> seitr = op.GetInsertPosEndSourceIterator();
					while (seitr.hasNext())
					{
						int posline = seitr.next();
						InsertLocationSearchVisitor ilsv = new InsertLocationSearchVisitor(posline, true);
						methodblock.accept(ilsv);
						ASTNode insertnode = ilsv.getInsertnode();
						MethodInvocation newInvocation = ast.newMethodInvocation();
						newInvocation.setName(ast.newSimpleName("unlock"));
						newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool."+lockname));
						Statement newStatement = ast.newExpressionStatement(newInvocation);
						listRewrite.insertBefore(insertnode, newStatement, null);
					}
				}
			}
		}
		Set<String> keys = allrewrites.keySet();
		Iterator<String> kitr = keys.iterator();
		while (kitr.hasNext())
		{
			String mtype = kitr.next();
			ASTRewrite aw = allrewrites.get(mtype);
			TextEdit edits = aw.rewriteAST();
			File f = GetMostMatchFile(mtype);
			Document document = new Document(GetFileContent(mtype));
			edits.apply(document);
			FileUtil.ClearAndWriteToFile(document.get(), f);
		}
	}
	
	private CompilationUnit GetCompilationUnit(String msig)
	{
		String mtype = NameUtil.GetClassNameFromMethodSig(msig);
		CompilationUnit cu = cus.get(mtype);
		if (cu == null)
		{
			Document document = new Document(GetFileContent(mtype));
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(document.get().toCharArray());
			cu = (CompilationUnit) parser.createAST(null);
			cus.put(mtype, cu);
		}
		return cu;
	}
	
	private ASTRewrite GetASTRewriteAccordingToMethodSig(String msig, AST ast)
	{
		String mtype = NameUtil.GetClassNameFromMethodSig(msig);
		ASTRewrite aw = allrewrites.get(mtype);
		if (aw == null)
		{
			aw = ASTRewrite.create(ast);
			allrewrites.put(mtype, aw);
		}
		return aw;
	}
	
	private String GetFileContent(String mtype)
	{
		File f = GetMostMatchFile(mtype);
		String fcontent = filecontent.get(mtype);
		if (fcontent == null)
		{
			fcontent = FileUtil.ReadFileByLines(f);
			filecontent.put(mtype, fcontent);
		}
		return fcontent;
	}
	
	private File GetMostMatchFile(String mtype)
	{
		File f = exactmatchfile.get(mtype);
		if (f == null)
		{
			String path = mtype.replace('.', '/')+".java";
			Set<String> keys = allfiles.keySet();
			Iterator<String> itr = keys.iterator();
			f = null;
			while (itr.hasNext())
			{
				String fpath = itr.next();
				
				System.out.println("fpath:"+fpath);
				
				if (fpath.endsWith(path))
				{
					f = allfiles.get(fpath);
				}
			}
			if (f == null)
			{
				System.err.println("There is no corresponding source file in directory of class:"+mtype+". The system will exit.");
				new Exception().printStackTrace();
				System.exit(1);
			}
			exactmatchfile.put(mtype, f);
		}
		return f;
	}
	
}
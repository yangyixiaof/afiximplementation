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
import org.eclipse.jdt.core.dom.ImportDeclaration;
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
	
	// key is exact file absolute path
	Map<String, String> filecontent = new TreeMap<String, String>();
	Map<String, Document> docs = new TreeMap<String, Document>();
	Map<String, ASTRewrite> allrewrites = new TreeMap<String, ASTRewrite>();
	Map<String, CompilationUnit> cus = new TreeMap<String, CompilationUnit>();
	Map<String, AST> asts = new TreeMap<String, AST>();
	
	public SourceFileModifier(String projectname) {
		FileUtil.GetAllFilesInADirectory(new File(projectname), allfiles);
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
				AST ast = GetAST(msig);
				ASTRewrite aw = GetASTRewriteAccordingToMethodSig(msig, ast);
				SearchOrder so = new SearchOrder(msig);
				BlockLocationSearchVisitor blvisitor = new BlockLocationSearchVisitor(so);
				cu.accept(blvisitor);
				Block methodblock = blvisitor.getResult();
				
				if (methodblock == null)
				{
					System.out.println("methodblock:"+methodblock);
					new Exception("methodblock null, the system will exit.").printStackTrace();
					System.exit(1);
				}
				
				{
					Iterator<Integer> sbitr = op.GetInsertPosBeginSourceIterator();
					while (sbitr.hasNext())
					{
						int posline = sbitr.next() - 1;
						int poslineoff =  FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(), posline-1, GetFileContent(mtype));
						// testing
						// poslineoff =  FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(), 1, GetFileContent(mtype));
						// testing
						// System.out.println("cu start pos:"+cu.getStartPosition()+";beginpos:"+posline+";poslineoff:"+poslineoff+";filecontent:"+GetFileContent(mtype));
						InsertLocationSearchVisitor ilsv = new InsertLocationSearchVisitor(poslineoff, true, methodblock);
						methodblock.accept(ilsv);
						ASTNode insertnode = ilsv.getInsertnode();
						Block ib = ilsv.getInsertblock();
						ListRewrite listRewrite = aw.getListRewrite(ib, Block.STATEMENTS_PROPERTY);
						MethodInvocation newInvocation = ast.newMethodInvocation();
						newInvocation.setName(ast.newSimpleName("lock"));
						newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool."+lockname));
						Statement newStatement = ast.newExpressionStatement(newInvocation);
						
						// testing
						System.out.println("msig:"+msig+";posline:"+posline+";insertnodeBegin:"+insertnode+";insertnodestartpos:"+insertnode.getStartPosition()+";insertnodeendpos:"+(insertnode.getStartPosition()+insertnode.getLength()));
						
						listRewrite.insertBefore(newStatement, insertnode, null);
					}
				}
				
				{
					Iterator<Integer> seitr = op.GetInsertPosEndSourceIterator();
					while (seitr.hasNext())
					{
						int posline = seitr.next() - 1;
						int poslineoff =  FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(), posline, GetFileContent(mtype));
						InsertLocationSearchVisitor ilsv = new InsertLocationSearchVisitor(poslineoff, false, methodblock);
						methodblock.accept(ilsv);
						ASTNode insertnode = ilsv.getInsertnode();
						Block ib = ilsv.getInsertblock();
						ListRewrite listRewrite = aw.getListRewrite(ib, Block.STATEMENTS_PROPERTY);
						MethodInvocation newInvocation = ast.newMethodInvocation();
						newInvocation.setName(ast.newSimpleName("unlock"));
						newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool."+lockname));
						Statement newStatement = ast.newExpressionStatement(newInvocation);
						
						// testing
						System.out.println("posline:"+posline+";insertnodeEnd:"+insertnode+";insertnodestartpos:"+insertnode.getStartPosition()+";insertnodeendpos:"+(insertnode.getStartPosition()+insertnode.getLength()));
						
						listRewrite.insertAfter(newStatement, insertnode, null);
					}
				}
			}
		}
		Set<String> keys = allrewrites.keySet();
		
		System.out.println("allrewrites size:" + allrewrites.size());
		
		Iterator<String> kitr = keys.iterator();
		while (kitr.hasNext())
		{
			String fabpath = kitr.next();
			ASTRewrite aw = allrewrites.get(fabpath);
			Document document = docs.get(fabpath);
			TextEdit edits = aw.rewriteAST(document, null);
			edits.apply(document);
			FileUtil.ClearAndWriteToFile(document.get(), new File(fabpath));
			// TextEdit edits = aw.rewriteAST();
			// File f = GetMostMatchFile(mtype);
			// Document document = new Document(GetFileContent(mtype));
			// edits.apply(document);
			// FileUtil.ClearAndWriteToFile(document.get(), f);
		}
	}
	
	private AST GetAST(String msig) {
		String mtype = NameUtil.GetClassNameFromMethodSig(msig);
		File f = GetMostMatchFile(mtype);
		AST res = asts.get(f.getAbsolutePath());
		if (res == null)
		{
			res = GetCompilationUnit(msig).getAST();
			asts.put(f.getAbsolutePath(), res);
		}
		return res;
	}

	private CompilationUnit GetCompilationUnit(String msig)
	{
		String mtype = NameUtil.GetClassNameFromMethodSig(msig);
		File f = GetMostMatchFile(mtype);
		CompilationUnit cu = cus.get(f.getAbsolutePath());
		if (cu == null)
		{
			Document document = new Document(GetFileContent(mtype));
			docs.put(f.getAbsolutePath(), document);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(document.get().toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			cu = (CompilationUnit) parser.createAST(null);
			cus.put(f.getAbsolutePath(), cu);
			AST ast = GetAST(msig);
			ASTRewrite aw = GetASTRewriteAccordingToMethodSig(msig, ast);
			cu.recordModifications();
			ImportDeclaration id = ast.newImportDeclaration();
			id.setName(ast.newName(new String[] {"java", "util", "concurrent", "locks", "Lock"}));
			ListRewrite lrw = aw.getListRewrite(cu, CompilationUnit.IMPORTS_PROPERTY);
			lrw.insertLast(id, null);
		}
		return cu;
	}
	
	private ASTRewrite GetASTRewriteAccordingToMethodSig(String msig, AST ast)
	{
		String mtype = NameUtil.GetClassNameFromMethodSig(msig);
		File f = GetMostMatchFile(mtype);
		ASTRewrite aw = allrewrites.get(f.getAbsolutePath());
		if (aw == null)
		{
			aw = ASTRewrite.create(ast);
			allrewrites.put(f.getAbsolutePath(), aw);
		}
		return aw;
	}
	
	private String GetFileContent(String mtype)
	{
		File f = GetMostMatchFile(mtype);
		String fcontent = filecontent.get(f.getAbsolutePath());
		if (fcontent == null)
		{
			fcontent = FileUtil.ReadFileByLines(f);
			filecontent.put(f.getAbsolutePath(), fcontent);
		}
		return fcontent;
	}
	
	private File GetMostMatchFile(String mtype)
	{
		System.out.println("mtype:"+mtype);
		File f = exactmatchfile.get(mtype);
		if (f == null)
		{
			String rawpath = mtype.replace('.', '/');
			int idx = rawpath.indexOf('$');
			if (idx != -1)
			{
				rawpath = rawpath.substring(0, idx);
			}
			String path = rawpath+".java";
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
package cn.yyx.labtask.afix.classmodification;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
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
import cn.yyx.labtask.afix.gui.AFixEntity;
import cn.yyx.labtask.afix.gui.AFixFactory;
import cn.yyx.labtask.afix.patchgeneration.ExclusivePatchesManager;
import cn.yyx.labtask.afix.patchgeneration.OnePatch;
import cn.yyx.labtask.afix.patchgeneration.SameLockExclusivePatches;

public class SourceFileModifier {

	IJavaProject project = null;

	// String sourcefolder = null;
	Map<String, File> allfiles = new TreeMap<String, File>();
	Map<String, File> exactmatchfile = new TreeMap<String, File>();

	// key is exact file absolute path
	Map<String, String> filecontent = new TreeMap<String, String>();
	Map<String, Document> docs = new TreeMap<String, Document>();
	Map<String, ASTRewrite> allrewrites = new TreeMap<String, ASTRewrite>();
	Map<String, CompilationUnit> cus = new TreeMap<String, CompilationUnit>();
	Map<String, AST> asts = new TreeMap<String, AST>();

	// Map<String, LinkedList<Integer>> initialpositions = new TreeMap<String,
	// LinkedList<Integer>>();
	// Map<String, LinkedList<Integer>> actualpositions = new TreeMap<String,
	// LinkedList<Integer>>();
	// Map<String, LinkedList<Boolean>> positionislock = new TreeMap<String,
	// LinkedList<Boolean>>();
	// Map<String, String> positionlocknames = new TreeMap<String, String>();

	// Map<String, LinkedList<ASTNode>> lockmap = new TreeMap<String,
	// LinkedList<ASTNode>>();
	// Map<String, LinkedList<ASTNode>> unlockmap = new TreeMap<String,
	// LinkedList<ASTNode>>();

	Map<String, TreeMap<String, Boolean>> filelocks = new TreeMap<String, TreeMap<String, Boolean>>();
	Map<String, TreeMap<String, Boolean>> fileunlocks = new TreeMap<String, TreeMap<String, Boolean>>();

	// String projectname
	public SourceFileModifier(IJavaProject ijp) {
		this.project = ijp;
		String abspath = ijp.getProject().getLocation().toFile().getAbsolutePath();
		File absf = new File(abspath + "/" + "src");
		if (!absf.exists()) {
			System.err.println("No src? What the fuck?");
			System.exit(1);
		}
		FileUtil.GetAllFilesInADirectory(new File(absf.getAbsolutePath()), allfiles);
	}

	public void HandleExclusivePatchesManager(ExclusivePatchesManager epm) throws InvalidClassFileException,
			JavaModelException, IllegalArgumentException, MalformedTreeException, BadLocationException {
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

				String fileunique = GetFileUnique(mtype);

				// LinkedList<Integer> inip = initialpositions.get(fileunique);
				// if (inip == null)
				// {
				// inip = new LinkedList<Integer>();
				// initialpositions.put(fileunique, inip);
				// }
				// LinkedList<Integer> ap = actualpositions.get(fileunique);
				// if (ap == null)
				// {
				// ap = new LinkedList<Integer>();
				// actualpositions.put(fileunique, ap);
				// }
				// LinkedList<Boolean> pil = positionislock.get(fileunique);
				// if (pil == null)
				// {
				// pil = new LinkedList<Boolean>();
				// positionislock.put(fileunique, pil);
				// }

				SearchOrder so = new SearchOrder(msig);
				BlockLocationSearchVisitor blvisitor = new BlockLocationSearchVisitor(so);
				cu.accept(blvisitor);
				Block methodblock = blvisitor.getResult();

				if (methodblock == null) {
					System.out.println("methodblock:" + methodblock);
					new Exception("methodblock null, the system will exit.").printStackTrace();
					System.exit(1);
				}

				{
					Iterator<Integer> sbitr = op.GetInsertPosBeginSourceIterator();
					while (sbitr.hasNext()) {
						int posline = sbitr.next() - 1;
						int poslineoff = FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(), posline - 1,
								GetFileContent(mtype));
						// testing
						// poslineoff =
						// FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(),
						// 1, GetFileContent(mtype));
						// testing
						// System.out.println("cu start
						// pos:"+cu.getStartPosition()+";beginpos:"+posline+";poslineoff:"+poslineoff+";filecontent:"+GetFileContent(mtype));
						InsertLocationSearchVisitor ilsv = new InsertLocationSearchVisitor(poslineoff, true,
								methodblock);
						methodblock.accept(ilsv);
						ASTNode insertnode = ilsv.getInsertnode();
						Block ib = ilsv.getInsertblock();
						ListRewrite listRewrite = aw.getListRewrite(ib, Block.STATEMENTS_PROPERTY);
						MethodInvocation newInvocation = ast.newMethodInvocation();
						newInvocation.setName(ast.newSimpleName("lock"));
						newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool." + lockname));
						Statement newStatement = ast.newExpressionStatement(newInvocation);

						// testing
						System.out.println("msig:" + msig + ";posline:" + posline + ";insertnodeBegin:" + insertnode
								+ ";insertnodestartpos:" + insertnode.getStartPosition() + ";insertnodeendpos:"
								+ (insertnode.getStartPosition() + insertnode.getLength()));

						listRewrite.insertBefore(newStatement, insertnode, null);

						PutMapAndValueList(filelocks, fileunique, lockname);
						// PutMapAndValueList(lockmap, lockname, newStatement);
						// int lineNumber =
						// cu.getLineNumber(insertnode.getStartPosition() - 1) -
						// 1;
						// positionlocknames.put(fileunique + ":" + lineNumber,
						// lockname);
						// HandleInitialAndActualPositions(lineNumber, inip, ap,
						// pil, true);
					}
				}

				{
					Iterator<Integer> seitr = op.GetInsertPosEndSourceIterator();
					while (seitr.hasNext()) {
						int posline = seitr.next() - 1;
						int poslineoff = FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(), posline,
								GetFileContent(mtype));
						InsertLocationSearchVisitor ilsv = new InsertLocationSearchVisitor(poslineoff, false,
								methodblock);
						methodblock.accept(ilsv);
						ASTNode insertnode = ilsv.getInsertnode();
						Block ib = ilsv.getInsertblock();
						ListRewrite listRewrite = aw.getListRewrite(ib, Block.STATEMENTS_PROPERTY);
						MethodInvocation newInvocation = ast.newMethodInvocation();
						newInvocation.setName(ast.newSimpleName("unlock"));
						newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool." + lockname));
						Statement newStatement = ast.newExpressionStatement(newInvocation);

						// testing
						System.out.println("posline:" + posline + ";insertnodeEnd:" + insertnode
								+ ";insertnodestartpos:" + insertnode.getStartPosition() + ";insertnodeendpos:"
								+ (insertnode.getStartPosition() + insertnode.getLength()));

						listRewrite.insertAfter(newStatement, insertnode, null);

						PutMapAndValueList(fileunlocks, fileunique, lockname);
						// PutMapAndValueList(unlockmap, lockname,
						// newStatement);
						// int lineNumber =
						// cu.getLineNumber(insertnode.getStartPosition() +
						// insertnode.getLength()) - 1;
						// positionlocknames.put(fileunique + ":" + lineNumber,
						// lockname);
						// HandleInitialAndActualPositions(lineNumber, inip, ap,
						// pil, false);
					}
				}
			}
		}

		// testing
		System.out.println("allrewrites size:" + allrewrites.size());

		AFixFactory.CLear();

		Set<String> keys = allrewrites.keySet();
		Iterator<String> kitr = keys.iterator();
		while (kitr.hasNext()) {
			String fabpath = kitr.next();
			ASTRewrite aw = allrewrites.get(fabpath);
			Document document = docs.get(fabpath);
			TextEdit edits = aw.rewriteAST(document, null);
			edits.apply(document);
			File df = new File(fabpath);
			FileUtil.ClearAndWriteToFile(document.get(), df);
			CompilationUnit dcu = GetCompilationUnit(df);

			{
				Set<String> lkeys = filelocks.keySet();
				Iterator<String> litr = lkeys.iterator();
				while (litr.hasNext()) {
					String fpath = litr.next();
					TreeMap<String, Boolean> fls = filelocks.get(fpath);
					GenerateAFixEntries(dcu, fls, fpath, true);
					// LinkedList<ASTNode> ll = lockmap.get(lockname);
					// GenerateAFixEntries(lockname, dcu, ll, fabpath, true);
					// LinkedList<ASTNode> ul = unlockmap.get(lockname);
					// GenerateAFixEntries(lockname, dcu, ul, fabpath, false);
				}
			}

			{
				Set<String> ukeys = fileunlocks.keySet();
				Iterator<String> uitr = ukeys.iterator();
				while (uitr.hasNext()) {
					String fpath = uitr.next();
					TreeMap<String, Boolean> fls = fileunlocks.get(fpath);
					GenerateAFixEntries(dcu, fls, fpath, false);
				}
			}

			// LinkedList<Integer> inip = initialpositions.get(fabpath);
			// LinkedList<Integer> ap = actualpositions.get(fabpath);
			// LinkedList<Boolean> pil = positionislock.get(fabpath);
			// if (inip == null || ap == null || pil == null)
			// {
			// System.err.println("No position?");
			// System.exit(1);
			// }
			// Iterator<Integer> iitr = inip.iterator();
			// Iterator<Integer> aitr = ap.iterator();
			// Iterator<Boolean> pitr = pil.iterator();
			// while (aitr.hasNext())
			// {
			// Integer ii = iitr.next();
			// Integer ai = aitr.next();
			// Boolean pi = pitr.next();
			// String lockfullnamekey = fabpath + ":" + ii;
			// String lockname = positionlocknames.get(lockfullnamekey);
			// String lockfullnamelocation = fabpath + ":" + ai;
			// int lidx = lockfullnamelocation.indexOf('/');
			// if (lidx == -1)
			// {
			// lidx = lockfullnamelocation.indexOf('\\');
			// }
			// AFixFactory.AddEntry(new AFixEntity(lockname, pi ? "lock" :
			// "unlock", lockfullnamelocation.substring(lidx + 1),
			// lockfullnamelocation));
			// }
		}

		// set the content of LockPool.java.
		IFolder sourceFolder = project.getProject().getFolder("src");
		// IPackageFragmentRoot root =
		// project.getPackageFragmentRoot(sourceFolder);
		IPackageFragment pack = project.getPackageFragmentRoot(sourceFolder)
				.createPackageFragment("cn.yyx.labtask.afix", true, null);
		StringBuffer buffer = new StringBuffer();
		buffer.append("package " + pack.getElementName() + ";\n");
		buffer.append("\n");
		buffer.append("import java.util.concurrent.locks.Lock;\n");
		buffer.append("import java.util.concurrent.locks.ReentrantLock;\n");
		buffer.append("\n");
		buffer.append("public class LockPool {\n");
		for (int i = 0; i < lockidx; i++) {
			String lockname = "lock" + (i + 1);
			buffer.append("\t" + "public static" + " " + "Lock" + " " + lockname + " = new ReentrantLock();\n");
		}
		buffer.append("}\n");
		ICompilationUnit cu = pack.createCompilationUnit("LockPool.java", buffer.toString(), true, null);
		assert cu != null;
	}

	private void GenerateAFixEntries(CompilationUnit cu, TreeMap<String, Boolean> lks, String fabpath, boolean islock) {
		Set<String> ks = lks.keySet();
		Iterator<String> itr = ks.iterator();
		while (itr.hasNext()) {
			String lockname = itr.next();
			List<Integer> linenumbers = null;
			if (islock) {
				linenumbers = ASTHelper.GetLockASTNodeLineNumber(cu, lockname);
			} else {
				linenumbers = ASTHelper.GetUnLockASTNodeLineNumber(cu, lockname);
			}
			Iterator<Integer> litr = linenumbers.iterator();
			while (litr.hasNext()) {
				int linenumber = litr.next();
				String lockfullnamelocation = fabpath + ":" + linenumber;
				int lidx = lockfullnamelocation.indexOf('/');
				if (lidx == -1) {
					lidx = lockfullnamelocation.indexOf('\\');
				}
				AFixFactory.AddEntry(new AFixEntity(lockname, islock ? "lock" : "unlock",
						lockfullnamelocation.substring(lidx + 1), lockfullnamelocation));
			}
		}
	}

	private void PutMapAndValueList(Map<String, TreeMap<String, Boolean>> kmap, String lockname, String kname) {
		TreeMap<String, Boolean> kl = kmap.get(lockname);
		if (kl == null) {
			kl = new TreeMap<String, Boolean>();
			kmap.put(lockname, kl);
		}
		kl.put(kname, true);
	}

	/*
	 * private void PutMapAndValueList(Map<String, LinkedList<ASTNode>> kmap,
	 * String lockname, ASTNode insertnode) { LinkedList<ASTNode> kl =
	 * kmap.get(lockname); if (kl == null) { kl = new LinkedList<ASTNode>();
	 * kmap.put(lockname, kl); } kl.add(insertnode); }
	 * 
	 * private void HandleInitialAndActualPositions(int lineNumber,
	 * LinkedList<Integer> inip, LinkedList<Integer> ap, LinkedList<Boolean>
	 * pil, boolean islock) { int idx = 0; Iterator<Integer> iitr =
	 * inip.iterator(); while (iitr.hasNext()) { Integer ii = iitr.next(); if
	 * (ii > lineNumber) { break; } idx++; } pil.add(idx, islock); inip.add(idx,
	 * lineNumber); ap.add(idx, lineNumber + idx); int aidx = 0;
	 * Iterator<Integer> aitr = ap.iterator(); while (aitr.hasNext()) { Integer
	 * ai = aitr.next(); if (aidx > idx) { ap.set(aidx, ai+1); } aidx++; } }
	 */

	private AST GetAST(String msig) {
		String mtype = NameUtil.GetClassNameFromMethodSig(msig);
		File f = GetMostMatchFile(mtype);
		AST res = asts.get(f.getAbsolutePath());
		if (res == null) {
			res = GetCompilationUnit(msig).getAST();
			asts.put(f.getAbsolutePath(), res);
		}
		return res;
	}

	private CompilationUnit GetCompilationUnit(String msig) {
		String mtype = NameUtil.GetClassNameFromMethodSig(msig);
		File f = GetMostMatchFile(mtype);
		CompilationUnit cu = cus.get(f.getAbsolutePath());
		if (cu == null) {
			Document document = new Document(GetFileContent(mtype));
			docs.put(f.getAbsolutePath(), document);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(document.get().toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			cu = (CompilationUnit) parser.createAST(null);
			cus.put(f.getAbsolutePath(), cu);
			// AST ast = GetAST(msig);
			// ASTRewrite aw = GetASTRewriteAccordingToMethodSig(msig, ast);
			// cu.recordModifications();
			// ImportDeclaration id = ast.newImportDeclaration();
			// id.setName(ast.newName(new String[] {"java", "util",
			// "concurrent", "locks", "Lock"}));
			// ListRewrite lrw = aw.getListRewrite(cu,
			// CompilationUnit.IMPORTS_PROPERTY);
			// lrw.insertLast(id, null);
		}
		return cu;
	}

	private CompilationUnit GetCompilationUnit(File df) {
		Document document = new Document(FileUtil.ReadFileByLines(df));
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(document.get().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}

	private ASTRewrite GetASTRewriteAccordingToMethodSig(String msig, AST ast) {
		String mtype = NameUtil.GetClassNameFromMethodSig(msig);
		File f = GetMostMatchFile(mtype);
		ASTRewrite aw = allrewrites.get(f.getAbsolutePath());
		if (aw == null) {
			aw = ASTRewrite.create(ast);
			allrewrites.put(f.getAbsolutePath(), aw);
		}
		return aw;
	}

	private String GetFileContent(String mtype) {
		File f = GetMostMatchFile(mtype);
		String fcontent = filecontent.get(f.getAbsolutePath());
		if (fcontent == null) {
			fcontent = FileUtil.ReadFileByLines(f);
			filecontent.put(f.getAbsolutePath(), fcontent);
		}
		return fcontent;
	}

	private String GetFileUnique(String mtype) {
		File f = GetMostMatchFile(mtype);
		return f.getAbsolutePath();
	}

	private File GetMostMatchFile(String mtype) {
		System.out.println("mtype:" + mtype);
		File f = exactmatchfile.get(mtype);
		if (f == null) {
			String rawpath = mtype.replace('.', '/');
			int idx = rawpath.indexOf('$');
			if (idx != -1) {
				rawpath = rawpath.substring(0, idx);
			}
			String path = rawpath + ".java";
			Set<String> keys = allfiles.keySet();
			Iterator<String> itr = keys.iterator();
			f = null;
			while (itr.hasNext()) {
				String fpath = itr.next();

				System.out.println("fpath:" + fpath);

				if (fpath.endsWith(path)) {
					f = allfiles.get(fpath);
				}
			}
			if (f == null) {
				System.err.println("There is no corresponding source file in directory of class:" + mtype
						+ ". The system will exit.");
				new Exception().printStackTrace();
				System.exit(1);
			}
			exactmatchfile.put(mtype, f);
		}
		return f;
	}

}
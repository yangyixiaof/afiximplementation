package cn.yyx.labtask.afix.classmodification;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import org.eclipse.jdt.core.dom.SynchronizedStatement;
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
import cn.yyx.labtask.afix.gui.AtomFixesView;
import cn.yyx.labtask.afix.patchgeneration.ExclusivePatchesManager;
import cn.yyx.labtask.afix.patchgeneration.InsertPosition;
import cn.yyx.labtask.afix.patchgeneration.OnePatch;
import cn.yyx.labtask.afix.patchgeneration.SameLockExclusivePatches;

public class SourceFileModifier {

	IJavaProject project = null;

	// String sourcefolder = null;
	// key is the qualified full name of a class.
	Map<String, File> allfiles = new TreeMap<String, File>();
	// Map<String, File> exactmatchfile = new TreeMap<String, File>();

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

	Map<String, Integer> addedlocks = new TreeMap<String, Integer>();
	Map<String, Integer> addedunlocks = new TreeMap<String, Integer>();

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

		ModifyContent[] seps = new ModifyContent[epm.getSize() + 1];
		seps[0] = new ModifyContent(new IntegerWrapper(-1));

		Iterator<SameLockExclusivePatches> itr = epm.Iterator();
		int lockidx = 0;
		while (itr.hasNext()) {
			lockidx++;
			final String lockname = "lock" + lockidx;
			seps[lockidx] = new ModifyContent(new IntegerWrapper(lockidx));
			SameLockExclusivePatches slep = itr.next();
			Iterator<OnePatch> opitr = slep.GetIterator();
			while (opitr.hasNext()) {
				OnePatch op = opitr.next();
				op.GenerateLockUnlockSolutions();
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
					// Integer
					Iterator<InsertPosition> sbitr = op.GetInsertPosBeginSourceIterator();
					while (sbitr.hasNext()) {
						InsertPosition ip = sbitr.next();
						// int posline = ip.getPosition() - 1;
						// int poslineoff = FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(), posline - 1,
						//		GetFileContent(mtype));
						// testing
						// poslineoff =
						// FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(),
						// 1, GetFileContent(mtype));
						// testing
						// System.out.println("cu start
						// pos:"+cu.getStartPosition()+";beginpos:"+posline+";poslineoff:"+poslineoff+";filecontent:"+GetFileContent(mtype));
						InsertLocationSearchVisitor ilsv = new InsertLocationSearchVisitor(cu, ip.getRacevar(), ip.getLineNumber(), true,
								methodblock);
						methodblock.accept(ilsv);
						ilsv.ProcessInsertNode();
						ASTNode insertnode = ilsv.getInsertnode();
						Block ib = ilsv.getInsertblock();
						// ListRewrite listRewrite = aw.getListRewrite(ib, Block.STATEMENTS_PROPERTY);
						MethodInvocation newInvocation = ast.newMethodInvocation();
						newInvocation.setName(ast.newSimpleName("lock"));
						newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool." + lockname));
						// Statement newStatement =
						// ast.newExpressionStatement(newInvocation);

						// testing
						System.out.println("msig:" + msig + ";posline:" + ip.getLineNumber() + ";insertnodeBegin:" + insertnode
								+ ";insertnodestartpos:" + insertnode.getStartPosition() + ";insertnodeendpos:"
								+ (insertnode.getStartPosition() + insertnode.getLength()));

						// System.err.println("insertnode:" + insertnode +
						// "\n;listrewrite block:" + ib);

						String lockposition = fileunique + ":" + insertnode.getStartPosition();
						if (!addedlocks.containsKey(lockposition)) {
							// listRewrite.insertBefore(newStatement,
							// insertnode, null);
							addedlocks.put(lockposition, lockidx);
							// ,listRewrite
							seps[lockidx].getOms()
									.add(new OneModify(methodblock, ib, ast, aw, newInvocation, insertnode, true));
							PutMapAndValueList(filelocks, fileunique, lockname);
						} else {
							int tlidx = addedlocks.get(lockposition);
							seps[tlidx].getLockidx().AddConnectedIntegerWrapper(seps[lockidx].getLockidx(), true);
						}
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
					// Integer
					Iterator<InsertPosition> seitr = op.GetInsertPosEndSourceIterator();
					while (seitr.hasNext()) {
						InsertPosition ip = seitr.next();
						// int posline = ip.getLineNumber() - 1;
						// int poslineoff = FileUtil.GetTotalOffsetOfLineEnd(cu.getStartPosition(), posline,
						//		GetFileContent(mtype));
						InsertLocationSearchVisitor ilsv = new InsertLocationSearchVisitor(cu, ip.getRacevar(), ip.getLineNumber(), false,
								methodblock);
						methodblock.accept(ilsv);
						ilsv.ProcessInsertNode();
						ASTNode insertnode = ilsv.getInsertnode();
						Block ib = ilsv.getInsertblock();
						// ListRewrite listRewrite = aw.getListRewrite(ib, Block.STATEMENTS_PROPERTY);
						MethodInvocation newInvocation = ast.newMethodInvocation();
						newInvocation.setName(ast.newSimpleName("unlock"));
						newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool." + lockname));
						// Statement newStatement =
						// ast.newExpressionStatement(newInvocation);
						
						// testing
						System.out.println("posline:" + ip.getLineNumber() + ";insertnodeEnd:" + insertnode
								+ ";insertnodestartpos:" + insertnode.getStartPosition() + ";insertnodeendpos:"
								+ (insertnode.getStartPosition() + insertnode.getLength()));
						
						// System.err.println("insertnode:" + insertnode);
						
						String lockposition = fileunique + ":" + insertnode.getStartPosition();
						/*
						 * if (!addedunlocks.containsKey(lockposition)) {
						 * addedunlocks.put(lockposition, true);
						 * listRewrite.insertAfter(newStatement, insertnode,
						 * null); PutMapAndValueList(fileunlocks, fileunique,
						 * lockname); }
						 */
						if (!addedunlocks.containsKey(lockposition)) {
							// listRewrite.insertBefore(newStatement,
							// insertnode, null);
							addedunlocks.put(lockposition, lockidx);
							// listRewrite,
							seps[lockidx].getOms()
									.add(new OneModify(methodblock, ib, ast, aw, newInvocation, insertnode, false));
							PutMapAndValueList(fileunlocks, fileunique, lockname);
						} else {
							int tlidx = addedunlocks.get(lockposition);
							seps[tlidx].getLockidx().AddConnectedIntegerWrapper(seps[lockidx].getLockidx(), true);
						}
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

		// Handle variable seps, especially its IntegerWrapper.
		Set<IntegerWrapper> alliterated = new TreeSet<IntegerWrapper>();
		for (int i = 1; i < seps.length; i++) {
			Set<IntegerWrapper> oneiterated = new TreeSet<IntegerWrapper>();
			ModifyContent tmc = seps[i];
			IntegerWrapper tli = tmc.getLockidx();
			if (!alliterated.contains(tli)) {
				GenerateTheTrueRewrite(IterateToFindAllConnect(tli, oneiterated), oneiterated);
				alliterated.addAll(oneiterated);
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
			CompilationUnit dcu = ASTHelper.GetCompilationUnit(df);

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

		AtomFixesView.RefreshViewer();
	}

	private int IterateToFindAllConnect(IntegerWrapper start, Set<IntegerWrapper> iteraterecord) {
		int minlockidx = start.getIv();
		iteraterecord.add(start);
		Iterator<IntegerWrapper> itr = start.GetIterator();
		while (itr.hasNext()) {
			IntegerWrapper iw = itr.next();
			if (iteraterecord.contains(iw)) {
				continue;
			}
			int tempmin = IterateToFindAllConnect(iw, iteraterecord);
			if (minlockidx > tempmin) {
				minlockidx = tempmin;
			}
		}
		return minlockidx;
	}
	
	Map<Block, LinkedList<OneModify>> bom = new HashMap<Block, LinkedList<OneModify>>();
	
	private void GenerateTheTrueRewrite(int minlockidx, Set<IntegerWrapper> iteraterecord) {
		InitialBom(iteraterecord);
		Set<Block> bkeys = bom.keySet();
		Iterator<Block> bitr = bkeys.iterator();
		while (bitr.hasNext())
		{
			Block bk = bitr.next();
			LinkedList<OneModify> omlist = SortOneModifyList(bom.get(bk));
			AnalysisRewrite(minlockidx, omlist);
		}
	}
	
	private void AnalysisRewrite(int minlockidx, LinkedList<OneModify> omlist) {
		List<OneModify> sstart = new LinkedList<OneModify>();
		List<OneModify> send = new LinkedList<OneModify>();
		
		boolean beforeisinsertbefore = true;
		Iterator<OneModify> itr = omlist.iterator();
		while (itr.hasNext())
		{
			OneModify om = itr.next();
			if (om.isIsinsertbefore()) {
				if (!beforeisinsertbefore) {
					GenerateRewrite(minlockidx, sstart, send);
					sstart.clear();
					send.clear();
				}
				sstart.add(om);
				beforeisinsertbefore = true;
			} else {
				send.add(om);
				beforeisinsertbefore = false;
			}
		}
		GenerateRewrite(minlockidx, sstart, send);
	}
	
	private void GenerateRewrite(final int minlockidx, final List<OneModify> sstart, final List<OneModify> send)
	{
		if ((sstart.size() == 0 && send.size() != 0) || (sstart.size() != 0 && send.size() == 0))
		{
			System.err.println("Wrong mechanism of lock and unlock.");
			System.exit(1);
		}
		if (sstart.size() == 0 && send.size() == 0)
		{
			return;
		}
		List<OneModify> sall = new LinkedList<OneModify>();
		sall.addAll(sstart);
		sall.addAll(send);
		Iterator<OneModify> sitr = sall.iterator();
		
		OneModify om1 = sitr.next();
		
		Block firstbk = om1.getIBlock();
		ASTNode firstins = om1.getInsertnode();
		
		Block secondbk = null;
		ASTNode secondins = null;
		
		while (sitr.hasNext())
		{
			OneModify omt = sitr.next();
			Block tempsecondbk = omt.getIBlock();
			ASTNode tempsecondins = omt.getInsertnode();
			
			while (!JudgeFirstIsAtLeastSameOrHigherLevelOfSecondLevel(firstbk, tempsecondbk, om1.getMethodDeclarationBlock()))
			{
				ASTNode temp = firstbk.getParent();
				firstins = firstins.getParent();
				while (!(temp instanceof Block))
				{
					temp = firstbk.getParent();
					firstins = firstins.getParent();
				}
				firstbk = (Block) temp;
			}
			
			ASTNode temp = tempsecondbk;
			while (temp != firstbk)
			{
				temp = temp.getParent();
				tempsecondins = tempsecondins.getParent();
			}
			tempsecondbk = (Block) temp;
			secondbk = tempsecondbk;
			secondins = tempsecondins;
		}
		
		if (firstbk != secondbk)
		{
			System.err.println("What the fuck, firstbk != secondbk???");
			System.exit(1);
		}
		
		// generate rewrite by first and second.
		// firstbk firstins secondbk secondins
		@SuppressWarnings("unchecked")
		List<Statement> stmts = firstbk.statements();
		List<Statement> trimedstmts = new LinkedList<Statement>();
		Iterator<Statement> itr = stmts.iterator();
		boolean start = false;
		boolean end = false;
		while (itr.hasNext() && !end)
		{
			Statement stmt = itr.next();
			if (stmt == firstins)
			{
				start = true;
			}
			if (start)
			{
				trimedstmts.add(stmt);	
			}
			if (stmt == secondins)
			{
				end = true;
			}
		}
		if (!end)
		{
			System.err.println("What the fuck, no end?");
			System.exit(1);
		}
		AST ast = om1.getAst();
		ASTRewrite aw = om1.getASTRewrite();
		
		SynchronizedStatement newsyn = ast.newSynchronizedStatement();
		newsyn.setExpression(ast.newSimpleName("haha"));
		Block bk = newsyn.getBody();
		ListRewrite firstbklistRewrite = aw.getListRewrite(firstbk, Block.STATEMENTS_PROPERTY);
		ListRewrite bkListRewrite = aw.getListRewrite(bk, Block.STATEMENTS_PROPERTY);
		Iterator<Statement> titr = trimedstmts.iterator();
		while (titr.hasNext())
		{
			Statement stmt = titr.next();
			bkListRewrite.insertLast(stmt, null);
			firstbklistRewrite.remove(stmt, null);
		}
		firstbklistRewrite.insertBefore(newsyn, firstins, null);
	}
	
	private boolean JudgeFirstIsAtLeastSameOrHigherLevelOfSecondLevel(final Block firstbk, final Block secondbk, final Block methoddeclare)
	{
		ASTNode temp = secondbk;
		while (temp != methoddeclare)
		{
			if (firstbk == temp)
			{
				return true;
			}
			temp = temp.getParent();
		}
		if (firstbk == temp)
		{
			return true;
		}
		return false;
	}
	
	private LinkedList<OneModify> SortOneModifyList(LinkedList<OneModify> omlist)
	{
		LinkedList<OneModify> omres = new LinkedList<OneModify>();
		Queue<OneModify> omque = new PriorityQueue<OneModify>();
		Iterator<OneModify> omitr = omlist.iterator();
		while (omitr.hasNext())
		{
			OneModify om = omitr.next();
			omque.add(om);
		}
		Iterator<OneModify> qitr = omque.iterator();
		while (qitr.hasNext())
		{
			OneModify om = qitr.next();
			omres.add(om);
		}
		return omres;
	}
	
	private void InitialBom(Set<IntegerWrapper> iteraterecord)
	{
		Iterator<IntegerWrapper> itr = iteraterecord.iterator();
		while (itr.hasNext()) {
			IntegerWrapper iw = itr.next();
			ModifyContent mc = iw.getMc();
			List<OneModify> oms = mc.getOms();
			Iterator<OneModify> omitr = oms.iterator();
			while (omitr.hasNext()) {
				OneModify om = omitr.next();
				LinkedList<OneModify> omlist = bom.get(om.getIBlock());
				if (omlist == null)
				{
					omlist = new LinkedList<OneModify>();
					bom.put(om.getMethodDeclarationBlock(), omlist);
				}
				omlist.add(om);
			}
		}
	}
	
	// two places need to be changed.
	// this is the first place.
	// private void GenerateTheTrueRewrite(int minlockidx, Set<IntegerWrapper> iteraterecord) {
	//	Iterator<IntegerWrapper> itr = iteraterecord.iterator();
	//	while (itr.hasNext()) {
	//		IntegerWrapper iw = itr.next();
	//		ModifyContent mc = iw.getMc();
	//		List<OneModify> oms = mc.getOms();
	//		Iterator<OneModify> omitr = oms.iterator();
	//		while (omitr.hasNext()) {
	//			OneModify om = omitr.next();
	//			ListRewrite listRewrite = om.getListRewrite();
	//			AST ast = om.getAst();
	//			MethodInvocation newInvocation = om.getNewInvocation();
	//			newInvocation.setExpression(ast.newName("cn.yyx.labtask.afix.LockPool.lock" + minlockidx));
	//			Statement newStatement = ast.newExpressionStatement(newInvocation);
	//			if (om.isIsinsertbefore()) {
	//				try {
	//					listRewrite.insertBefore(newStatement, om.getInsertnode(), null);
	//				} catch (Exception e) {
	//					System.err.println("ERROR: insert before node:" + om.getInsertnode());
	//					throw e;
	//				}
	//			} else {
	//				try {
	//					listRewrite.insertAfter(newStatement, om.getInsertnode(), null);
	//				} catch (Exception e) {
	//					System.err.println("ERROR: insert after node:" + om.getInsertnode());
	//					throw e;
	//				}
	//			}
	//		}
	//	}
	// }
	
	// TODO this is the second place.
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
				AFixFactory.AddEntry(new AFixEntity(lockname, islock ? "synchronization" : "synchronization", // "lock" : "unlock"
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
		// System.out.println("mtype:" + mtype);
		// File f = exactmatchfile.get(mtype);
		// if (f == null) {
		// String rawpath = mtype.replace('.', '/');
		int idx = mtype.indexOf('$');
		if (idx != -1) {
			mtype = mtype.substring(0, idx);
		}
		// String path = rawpath + ".java";
		// Set<String> keys = allfiles.keySet();
		// Iterator<String> itr = keys.iterator();
		// f = null;
		// while (itr.hasNext()) {
		// String fpath = itr.next();
		// System.out.println("fpath:" + fpath);
		// if (fpath.endsWith(path)) {
		// f = allfiles.get(fpath);
		// }
		// }
		File f = allfiles.get(mtype);
		if (f == null) {
			System.err.println(
					"There is no corresponding source file in directory of class:" + mtype + ". The system will exit.");
			new Exception().printStackTrace();
			System.exit(1);
		}
		// exactmatchfile.put(mtype, f);
		// }
		return f;
	}

}
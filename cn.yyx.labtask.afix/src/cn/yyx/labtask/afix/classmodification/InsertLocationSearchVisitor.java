package cn.yyx.labtask.afix.classmodification;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;

public class InsertLocationSearchVisitor extends ASTVisitor {

	private CompilationUnit compileunit = null;
	private ASTNode insertnode = null;
	private ASTNode processnode = null;
	private Block insertblock = null;
	private String racevar = null;
	private int linenumber = -1;
	private boolean before = false;
	private Block bigblock = null;
	private int startpos = -1;
	private int endpos = -1;
	private boolean sanalyze = false;
	private boolean sanalyzeend = false;
	private boolean runover = false;
	private Set<ASTNode> lowlevelnamenodes = new HashSet<ASTNode>();
	private Set<ASTNode> meetstatementnamenodes = new HashSet<ASTNode>();

	public InsertLocationSearchVisitor(CompilationUnit cu, String racevar, int linenumber, boolean before,
			Block bigblock) {
		this.compileunit = cu;
		this.racevar = racevar;
		this.linenumber = linenumber;
		this.before = before;
		this.bigblock = bigblock;
		InitialAnalysisRange();
	}

	private void InitialAnalysisRange() {
		startpos = compileunit.getPosition(linenumber, 0);
		endpos = compileunit.getPosition(linenumber + 1, 0);
		if (endpos == -1) {
			endpos = compileunit.getLength() - 1;
		} else {
			endpos--;
		}
		if (endpos < 0) {
			System.err.println("What the fuck! what the range?");
		}
	}

	public boolean preVisit2(ASTNode node) {
		// currlevel++;
		if (sanalyzeend || runover) {
			return false;
		}
		if (node == bigblock) {
			sanalyze = true;
		}
		if (node instanceof Name) {
			return false;
		}
		if (IsTwoFullBehindOne(startpos, endpos, node.getStartPosition(),
				node.getStartPosition() + node.getLength() - 1))
		{
			return false;
		}
		return super.preVisit2(node);
	}
	
	private boolean IsTwoFullBehindOne(int onestart, int oneend, int twostart, int twoend)
	{
		if (twostart >= oneend)
		{
			return true;
		}
		return false;
	}
	
	private boolean IsIntersected(int onestart, int oneend, int twostart, int twoend) {
		if ((onestart >= twostart && onestart <= twoend) || (oneend >= twostart && oneend <= twoend)
				|| (onestart >= twostart && oneend <= twoend) || (twostart >= onestart && twoend <= oneend)) {
			return true;
		}
		return false;
	}

	@Override
	public void postVisit(ASTNode node) {
		if (node == bigblock) {
			sanalyze = false;
			sanalyzeend = true;
		}
		boolean intersected = IsIntersected(startpos, endpos, node.getStartPosition(),
				node.getStartPosition() + node.getLength() - 1);
		if (!runover && node instanceof Name && sanalyze && racevar != null && intersected) {
			String nstr = ((Name) node).toString();
			if (nstr.endsWith("." + racevar) || nstr.equals(racevar)) {
				lowlevelnamenodes.add(node);
			}
		}
		if (!runover && node instanceof Statement && !(node instanceof EmptyStatement) && sanalyze && intersected) {
			if (racevar == null) {
				runover = true;
				setProcessnode(node);
			} else {
				if (before) {
					if (IsParentOfOneNode(lowlevelnamenodes, node)) {
						runover = true;
						setProcessnode(node);
					}
				} else {
					if (IsParentOfOneNode(lowlevelnamenodes, node)) {
						if (!IsParentOfOneNode(meetstatementnamenodes, node)) {
							setProcessnode(node);
							meetstatementnamenodes.add(node);
						}
					}
				}
			}
		}
		// currlevel--;
		super.postVisit(node);
	}

	private boolean IsParentOfOneNode(Set<ASTNode> anodes, ASTNode node) {
		Iterator<ASTNode> itr = anodes.iterator();
		while (itr.hasNext()) {
			ASTNode an = itr.next();
			ASTNode anparent = an.getParent();
			while (anparent != bigblock) {
				if (anparent == node) {
					return true;
				}
				anparent = anparent.getParent();
			}
		}
		return false;
	}

	// @Override
	// public boolean preVisit2(ASTNode node) {
	// if (node != bigblock && node instanceof Statement) {
	// testing.
	// System.out.println("==========begin=========");
	// System.out.println("node:" + node);
	// System.out.println("offsetfrombegining:" + offsetfrombegining +
	// ";startpos:" + node.getStartPosition()
	// + ";endpos:" + (node.getStartPosition() + node.getLength()));
	// System.out.println("==========end=========");
	// if (before) {
	// int startpos = node.getStartPosition();
	// if (startpos >= linenumber) {
	// if (recordpos == -1) {
	// recordpos = startpos;
	// setProcessnode(node);
	// }
	// return false;
	// else
	// {
	// if (recordpos > startpos) {
	// recordpos = startpos;
	// setInsertnodeAndBlock(node);
	// }
	// }
	// }
	// } else {
	// int endpos = node.getStartPosition() + node.getLength();
	// if (endpos <= linenumber && recordpos < endpos) {
	// recordpos = endpos;
	// setProcessnode(node);
	// return false;
	// }
	// }
	// }
	// return super.preVisit2(node);
	// }

	// @Override
	// public void postVisit(ASTNode node) {
	// if (!before && node != bigblock && node instanceof Statement) {
	// System.out.println("==========begin=========");
	// System.out.println("node:"+node);
	// System.out.println("offsetfrombegining:"+offsetfrombegining+";start
	// pos:"+node.getStartPosition()+";end
	// pos:"+(node.getStartPosition()+node.getLength()));
	// System.out.println("==========end=========");

	// int startpos = node.getStartPosition();
	// int endpos = node.getStartPosition() + node.getLength();
	// if (endpos >= linenumber && linenumber >= startpos) {
	// if (recordpos <= startpos)
	// {
	// recordpos = endpos;
	// setProcessnode(node);
	// }
	// }
	// }
	// super.postVisit(node);
	// }

	public ASTNode getInsertnode() {
		return insertnode;
	}

	private void setProcessnode(ASTNode processnode) {
		this.processnode = processnode;
	}
	
	public ASTNode getProcessnode() {
		return this.processnode;
	}

	public void ProcessInsertNode() {
		this.insertnode = this.processnode;
		ASTNode synnode = GetMostFarSynchronizedNode(insertnode);
		if (synnode != null) {
			this.insertnode = synnode;
		}
		ASTNode temp = this.insertnode.getParent();
		while (!(temp instanceof Block) && (temp != bigblock)) {
			temp = temp.getParent();
			this.insertnode = this.insertnode.getParent();
		}
		if (!(temp instanceof Block) || (temp == bigblock && !(this.insertnode instanceof Statement)))
		{
			System.err.println("What the fuck, the parent of insertnode is not kind of Blcoks?");
			System.exit(1);
		}
		this.insertblock = (Block) temp;
	}

	private ASTNode GetMostFarSynchronizedNode(ASTNode insertnode) {
		ASTNode synnode = null;
		ASTNode temp = insertnode.getParent();
		while (temp != null && temp != bigblock) {
			if (temp instanceof SynchronizedStatement) {
				synnode = temp;
			}
			temp = temp.getParent();
		}
		return synnode;
	}

	public Block getInsertblock() {
		return insertblock;
	}

}
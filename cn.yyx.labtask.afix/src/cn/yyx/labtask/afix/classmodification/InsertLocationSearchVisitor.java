package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;

public class InsertLocationSearchVisitor extends ASTVisitor {
	
	private CompilationUnit compileunit = null;
	private ASTNode insertnode = null;
	private ASTNode processnode = null;
	private Block insertblock = null;
	private String racevar = null;
	private int offsetfrombegining = -1;
	private boolean before = false;
	private int recordpos = -1;
	private Block bigblock = null;

	public InsertLocationSearchVisitor(CompilationUnit cu, String racevar, int offsetfrombegining, boolean before, Block bigblock) {
		this.compileunit = cu;
		this.racevar = racevar;
		this.offsetfrombegining = offsetfrombegining;
		this.before = before;
		this.bigblock = bigblock;
	}
	
	@Override
	public boolean preVisit2(ASTNode node) {
		if (node != bigblock && node instanceof Statement) {
			// testing.
			// System.out.println("==========begin=========");
			// System.out.println("node:" + node);
			// System.out.println("offsetfrombegining:" + offsetfrombegining + ";startpos:" + node.getStartPosition()
			//		+ ";endpos:" + (node.getStartPosition() + node.getLength()));
			// System.out.println("==========end=========");
			if (before) {
				int startpos = node.getStartPosition();
				if (startpos >= offsetfrombegining) {
					if (recordpos == -1) {
						recordpos = startpos;
						setProcessnode(node);
					}
					return false;
					// else
					// {
					// if (recordpos > startpos) {
					// recordpos = startpos;
					// setInsertnodeAndBlock(node);
					// }
					// }
				}
			} else {
				int endpos = node.getStartPosition() + node.getLength();
				if (endpos <= offsetfrombegining && recordpos < endpos) {
					recordpos = endpos;
					setProcessnode(node);
					return false;
				}
			}
		}
		return super.preVisit2(node);
	}
	
	@Override
	public void postVisit(ASTNode node) {
		if (!before && node != bigblock && node instanceof Statement) {
			// System.out.println("==========begin=========");
			// System.out.println("node:"+node);
			// System.out.println("offsetfrombegining:"+offsetfrombegining+";start
			// pos:"+node.getStartPosition()+";end
			// pos:"+(node.getStartPosition()+node.getLength()));
			// System.out.println("==========end=========");
			
			int startpos = node.getStartPosition();
			int endpos = node.getStartPosition() + node.getLength();
			if (endpos >= offsetfrombegining && offsetfrombegining >= startpos) {
				if (recordpos <= startpos)
				{
					recordpos = endpos;
					setProcessnode(node);
				}
			}
		}
		super.postVisit(node);
	}
	
	public ASTNode getInsertnode() {
		return insertnode;
	}
	
	private void setProcessnode(ASTNode processnode) {
		this.processnode = processnode;
	}

	public void ProcessInsertNode() {
		this.insertnode = this.processnode;
		ASTNode synnode = GetMostFarSynchronizedNode(insertnode);
		if (synnode != null) {
			this.insertnode = synnode;
		}
		ASTNode temp = this.insertnode.getParent();
		while (!(temp instanceof Block)) {
			temp = temp.getParent();
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
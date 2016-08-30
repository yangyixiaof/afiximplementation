package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;

public class InsertLocationSearchVisitor extends ASTVisitor {

	private ASTNode insertnode = null;
	private ASTNode processnode = null;
	private Block insertblock = null;
	int offsetfrombegining = -1;
	boolean before = false;
	int recordpos = -1;
	Block bigblock = null;

	public InsertLocationSearchVisitor(int offsetfrombegining, boolean before, Block bigblock) {
		this.offsetfrombegining = offsetfrombegining;
		this.before = before;
		this.bigblock = bigblock;
	}
	
	@Override
	public boolean preVisit2(ASTNode node) {
			if (node != bigblock && node instanceof Statement) {
				
				// System.out.println("==========begin=========");
				// System.out.println("node:"+node);
				// System.out.println("offsetfrombegining:"+offsetfrombegining+";start pos:"+node.getStartPosition()+";end pos:"+(node.getStartPosition()+node.getLength()));
				// System.out.println("==========end=========");
				
				int startpos = node.getStartPosition();
				int endpos = node.getStartPosition()+node.getLength();
				
				if (before) {
					if (startpos >= offsetfrombegining) {
						if (recordpos == -1) {
							recordpos = startpos;
							setInsertnodeAndBlock(node);
						} else {
							if (recordpos > startpos) {
								recordpos = startpos;
								setInsertnodeAndBlock(node);
							}
						}
					}
				} else {
					if (endpos <= offsetfrombegining) {
						if (recordpos == -1) {
							recordpos = endpos;
							setInsertnodeAndBlock(node);
						} else {
							if (recordpos < endpos) {
								recordpos = endpos;
								setInsertnodeAndBlock(node);
							}
						}
					} else {
						if (offsetfrombegining >= startpos) {
							recordpos = endpos;
							setInsertnodeAndBlock(node);
							return false;
						}
					}
				}
			}
		return super.preVisit2(node);
	}

	// @Override
	// public void postVisit(ASTNode node) {
	//	if (!before && node != bigblock)
	//	{
	//		if (node instanceof Statement) {
				// System.out.println("==========begin=========");
				// System.out.println("node:"+node);
				// System.out.println("offsetfrombegining:"+offsetfrombegining+";start pos:"+node.getStartPosition()+";end pos:"+(node.getStartPosition()+node.getLength()));
				// System.out.println("==========end=========");
	//		}
	//	}
	//	super.postVisit(node);
	// }
	
	public ASTNode getInsertnode() {
		return insertnode;
	}
	
	private void setInsertnodeAndBlock(ASTNode insertnode) {
		this.processnode = insertnode;
	}
	
	public void ProcessInsertNode()
	{
		this.insertnode = this.processnode;
		ASTNode synnode = GetMostFarSynchronizedNode(insertnode);
		if (synnode != null)
		{
			this.insertnode = synnode;
		}
		ASTNode temp = this.insertnode.getParent();
		while (!(temp instanceof Block))
		{
			temp = temp.getParent();
		}
		this.insertblock = (Block) temp;
	}
	
	private ASTNode GetMostFarSynchronizedNode(ASTNode insertnode)
	{
		ASTNode synnode = null;
		ASTNode temp = insertnode.getParent();
		while (temp != null && temp != bigblock)
		{
			if (temp instanceof SynchronizedStatement)
			{
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
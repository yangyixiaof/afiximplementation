package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;

public class InsertLocationSearchVisitor extends ASTVisitor {

	private ASTNode insertnode = null;

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
	public void preVisit(ASTNode node) {
		if (before && node != bigblock)
		{			
			if (node instanceof Statement) {
				
				// System.out.println("==========begin=========");
				// System.out.println("node:"+node);
				// System.out.println("offsetfrombegining:"+offsetfrombegining+";start pos:"+node.getStartPosition()+";end pos:"+(node.getStartPosition()+node.getLength()));
				// System.out.println("==========end=========");
				
				int startpos = node.getStartPosition();
				if (startpos >= offsetfrombegining) {
					if (recordpos == -1) {
						recordpos = startpos;
						insertnode = node;
					} else {
						if (recordpos > startpos) {
							recordpos = startpos;
							insertnode = node;
						}
					}
				}
			}
		}
		super.preVisit(node);
	}

	@Override
	public void postVisit(ASTNode node) {
		if (!before && node != bigblock)
		{
			if (node instanceof Statement) {
				
				// System.out.println("==========begin=========");
				// System.out.println("node:"+node);
				// System.out.println("offsetfrombegining:"+offsetfrombegining+";start pos:"+node.getStartPosition()+";end pos:"+(node.getStartPosition()+node.getLength()));
				// System.out.println("==========end=========");
				
				int endpos = node.getStartPosition()+node.getLength();
				if (endpos <= offsetfrombegining) {
					if (recordpos == -1) {
						recordpos = endpos;
						insertnode = node;
					} else {
						if (recordpos < endpos) {
							recordpos = endpos;
							insertnode = node;
						}
					}
				}
			}
		}
		super.postVisit(node);
	}

	public ASTNode getInsertnode() {
		return insertnode;
	}

	public void setInsertnode(ASTNode insertnode) {
		this.insertnode = insertnode;
	}

}
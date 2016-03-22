package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Statement;

public class InsertLocationSearchVisitor extends ASTVisitor {

	private ASTNode insertnode = null;

	int offsetfrombegining = -1;

	boolean before = false;

	int recordpos = -1;

	public InsertLocationSearchVisitor(int offsetfrombegining, boolean before) {
		this.offsetfrombegining = offsetfrombegining;
		this.before = before;
	}
	
	@Override
	public void preVisit(ASTNode node) {
		if (before)
		{
			if (node instanceof Statement) {
				int startpos = node.getStartPosition();
				if (startpos > offsetfrombegining) {
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
		if (!before)
		{
			if (node instanceof Statement) {
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
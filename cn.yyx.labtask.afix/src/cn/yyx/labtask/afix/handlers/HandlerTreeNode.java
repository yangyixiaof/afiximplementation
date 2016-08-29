package cn.yyx.labtask.afix.handlers;

import org.eclipse.jface.viewers.TreeNode;

public class HandlerTreeNode extends TreeNode implements Comparable<HandlerTreeNode> {
	
	public HandlerTreeNode(HandlerTask value) {
		super(value);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public int compareTo(HandlerTreeNode o) {
		return ((HandlerTask)value).compareTo((HandlerTask)o.value);
	}
	
}
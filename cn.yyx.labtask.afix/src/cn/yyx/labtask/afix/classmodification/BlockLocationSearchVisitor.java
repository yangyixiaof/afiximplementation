package cn.yyx.labtask.afix.classmodification;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;

import cn.yyx.labtask.afix.commonutil.SearchOrder;

public class BlockLocationSearchVisitor extends ASTVisitor{
	
	SearchOrder so = null;
	private Block result = null;
	
	public BlockLocationSearchVisitor(SearchOrder so) {
		this.so = so;
	}

	public Block getResult() {
		return result;
	}

	public void setResult(Block result) {
		this.result = result;
	}
	
}
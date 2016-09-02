package cn.yyx.labtask.afix.irengine;

import java.io.File;

import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.yyx.labtask.afix.classmodification.ASTHelper;

public class Test {
	
	public void heihei()
	{
		int x = 0;
		{
			if (x == 1)
			{
				x = 6;
			} else {
				x = 5;
			}
			System.err.println(x);
		}
		
		java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(Integer.MAX_VALUE);
		System.err.println(semaphore);
	}
	
	public static void main(String[] args) {
		CompilationUnit cu = ASTHelper.GetCompilationUnit(new File("SourceBackDir/IRGenerator.java"));
		System.err.println("line:" + cu.getLineNumber(0) + ";position:" + cu.getPosition(1, 2));
	}
	
}
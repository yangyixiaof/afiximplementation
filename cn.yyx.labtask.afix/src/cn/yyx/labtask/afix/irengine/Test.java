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
		System.err.println("total length:" + cu.getLength() + ";57,1 position:" + cu.getPosition(57, 1) + ";1,55 position:" + cu.getPosition(1, 55) + ";line:" + cu.getLineNumber(0) + ";position:" + cu.getPosition(3, 0) + ";col:" + cu.getColumnNumber(37));
	}
	
}
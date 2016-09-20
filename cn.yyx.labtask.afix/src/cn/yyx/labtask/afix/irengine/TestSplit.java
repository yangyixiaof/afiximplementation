package cn.yyx.labtask.afix.irengine;

import java.util.Arrays;

public class TestSplit {
	
	public static void main(String[] args) {
		String a = "atomicity/TestAtomicity1$1";
		System.out.println(Arrays.toString(a.split("\\$")));
		String b = "sd#ere#45#ar";
		System.out.println(Arrays.toString(b.split("#")));
	}
	
}

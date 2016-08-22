package cn.yyx.labtask.afix.patchgeneration;

public interface Mergeable {
	
	public Mergeable Merge(Mergeable t) throws Exception;
	
}
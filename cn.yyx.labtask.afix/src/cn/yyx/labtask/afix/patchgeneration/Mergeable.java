package cn.yyx.labtask.afix.patchgeneration;

public interface Mergeable<T> {
	
	public T Merge(T t) throws Exception;
	
}
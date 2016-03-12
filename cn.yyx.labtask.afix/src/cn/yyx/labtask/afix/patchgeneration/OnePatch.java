package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OnePatch {
	
	String methodsig = null;
	List<Integer> insertbeginidxs = new LinkedList<Integer>();
	List<Integer> insertendidxs = new LinkedList<Integer>();
	
	public OnePatch(String methodsig) {
		this.methodsig = methodsig;
	}
	
	public void AddLockBeforeIndex(Integer idx)
	{
		insertbeginidxs.add(idx);
	}
	
	public void AddUnlockAfterIndex(Integer idx)
	{
		insertendidxs.add(idx);
	}
	
	public Iterator<Integer> GetInsertPosEndIterator()
	{
		return insertendidxs.iterator();
	}
	
	public Iterator<Integer> GetInsertPosBeginIterator()
	{
		return insertbeginidxs.iterator();
	}
	
	/**
	 * 
	 * @param op
	 * @return is un-merged patch
	 */
	public OnePatch Merge(OnePatch op)
	{
		// TODO
		return null;
	}
	
}
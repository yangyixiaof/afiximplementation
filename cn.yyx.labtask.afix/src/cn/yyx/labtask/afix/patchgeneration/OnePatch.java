package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.labtask.afix.errordetection.ErrorTrace;

public class OnePatch {
	
	ErrorTrace et = null;
	String methodsig = null;
	List<Integer> insertbeginidxs = new LinkedList<Integer>();
	List<Integer> insertendidxs = new LinkedList<Integer>();
	
	public OnePatch(ErrorTrace et, String methodsig) {
		this.et = et;
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
	 * @param iop
	 * @return true merged; false un_merged.
	 */
	public boolean JudgeIntersectAndMerge(OnePatch iop) {
		
		
		
		return false;
	}
	
}
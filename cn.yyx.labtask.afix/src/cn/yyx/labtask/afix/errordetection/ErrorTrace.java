package cn.yyx.labtask.afix.errordetection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ErrorTrace {
	
	List<ErrorLocation> negativetrace = new LinkedList<ErrorLocation>();
	ArrayList<ErrorLocation> positivetrace = new ArrayList<ErrorLocation>();
	
	public void AddLocationAtPositiveOrder(ErrorLocation el)
	{
		positivetrace.add(el);
		negativetrace.add(0, el);
	}
	
	public void AddLocationAtNegativeOrder(ErrorLocation el)
	{
		positivetrace.add(0, el);
		negativetrace.add(el);
	}
	
	public Iterator<ErrorLocation> GetPositiveOrderIterator()
	{
		return positivetrace.iterator();
	}
	
	public Iterator<ErrorLocation> GetNegativeOrderIterator()
	{
		return negativetrace.iterator();
	}
	
}
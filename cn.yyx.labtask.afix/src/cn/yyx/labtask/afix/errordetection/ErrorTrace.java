package cn.yyx.labtask.afix.errordetection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ErrorTrace {
	
	List<ErrorLocation> negativetrace = new LinkedList<ErrorLocation>();
	List<ErrorLocation> positivetrace = new LinkedList<ErrorLocation>();
	
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("one trace:");
		Iterator<ErrorLocation> itr = positivetrace.iterator();
		while (itr.hasNext())
		{
			ErrorLocation el = itr.next();
			sb.append(el+";");
		}
		return sb.toString();
	}
	
}
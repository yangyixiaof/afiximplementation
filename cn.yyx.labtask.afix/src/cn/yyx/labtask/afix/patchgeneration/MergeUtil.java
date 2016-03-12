package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MergeUtil<T> {
	
	@SuppressWarnings("unchecked")
	public List<Mergeable<T>> Merge(List<Mergeable<T>> onelist) throws Exception
	{
		boolean intersected = true;
		List<Mergeable<T>> temp = null;
		List<Mergeable<T>> tempres = onelist;
		while (intersected)
		{
			intersected = false;
			temp = tempres;
			tempres = new LinkedList<Mergeable<T>>(); 
			Iterator<Mergeable<T>> itr = temp.iterator();
			int l1 = 0;
			while (itr.hasNext())
			{
				l1++;
				int l2 = 0;
				Mergeable<T> op = itr.next();
				Iterator<Mergeable<T>> itr2 = temp.iterator();
				while (itr2.hasNext())
				{
					l2++;
					if (l2 < l1)
					{
						continue;
					}
					Mergeable<T> op2 = itr2.next();
					if (op != op2)
					{
						Mergeable<T> tempmergere = (Mergeable<T>) op.Merge((T)op2);
						if (tempmergere != null)
						{
							// means intersected.
							intersected = true;
							tempres.add(tempmergere);
						}
						else
						{
							tempres.add(op);
							tempres.add(op2);
						}
					}
				}
			}
		}
		return temp;
	}
	
}

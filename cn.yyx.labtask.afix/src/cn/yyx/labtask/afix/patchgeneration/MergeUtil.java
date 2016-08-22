package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MergeUtil {
	
	/*@SuppressWarnings("unchecked")
	public List<Mergeable> Merge(List<Mergeable> onelist) throws Exception
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
			List<Mergeable<T>> merged = new LinkedList<Mergeable<T>>();
			while (itr.hasNext())
			{
				Mergeable<T> op = itr.next();
				l1++;
				int l2 = 0;
				if (HasMerged(op, merged))
				{
					continue;
				}
				// boolean ophandled = false;
				Iterator<Mergeable<T>> itr2 = temp.iterator();
				while (itr2.hasNext())
				{
					Mergeable<T> op2 = itr2.next();
					l2++;
					if (l2 <= l1)
					{
						continue;
					}
					if (op != op2)
					{
						Mergeable<T> tempmergere = (Mergeable<T>) op.Merge((T)op2);
						if (tempmergere != null)
						{
							// means intersected.
							intersected = true;
							// ophandled = true;
							merged.add(op2);
							// break;
						}
					}
				}
				tempres.add(op);
				// if (!ophandled)
				// {
				//	tempres.add(op);
				// }
			}
		}
		return temp;
	}

	private boolean HasMerged(Mergeable<T> op, List<Mergeable<T>> merged) {
		Iterator<Mergeable<T>> itr = merged.iterator();
		while (itr.hasNext())
		{
			Mergeable<T> mop = itr.next();
			if (op == mop)
			{
				return true;
			}
		}
		return false;
	}*/
	
	// SameLockExclusivePatches
	public static List<Mergeable> MergeList(List<Mergeable> patches) throws Exception
	{
		LinkedList<Mergeable> finalresult = new LinkedList<Mergeable>();
		boolean run = true;
		while (run)
		{
			LinkedList<Mergeable> result = new LinkedList<Mergeable>();
			boolean merged = FirstMergeToLeft(patches, result);
			if (merged) {
				patches = result;
			} else {
				finalresult.add(patches.get(0));
				if (patches.size() > 1) {
					patches = patches.subList(1, patches.size());
				} else {
					run = false;
				}
			}
		}
		return finalresult;
	}
	
	private static boolean FirstMergeToLeft(List<Mergeable> pts, List<Mergeable> result)
	{
		if (pts.size() <= 1)
		{
			return false;
		}
		Iterator<Mergeable> pitr = pts.iterator();
		Mergeable firstpatch = pitr.next();
		boolean merged = false;
		while (!merged && pitr.hasNext())
		{
			Mergeable lpatch = pitr.next();
			Mergeable mres = null;
			try {
				mres = firstpatch.Merge(lpatch);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			if (mres != null)
			{
				Iterator<Mergeable> itr = pts.iterator();
				while (itr.hasNext())
				{
					Mergeable patch = itr.next();
					if (patch != lpatch)
					{
						result.add(patch);
					}
				}
				merged = true;
			}
		}
		return merged;
	}
	
}
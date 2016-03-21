package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExclusivePatchesManager {
	
	List<SameLockExclusivePatches> patches = new LinkedList<SameLockExclusivePatches>();
	
	public ExclusivePatchesManager() {
	}
	
	public void AddOneExclusivePatch(SameLockExclusivePatches slep)
	{
		patches.add(slep);
	}
	
	
	public Iterator<SameLockExclusivePatches> Iterator()
	{
		return patches.iterator();
	}
	
	public void MergeSelf() throws Exception
	{
		List<SameLockExclusivePatches> res = new LinkedList<SameLockExclusivePatches>();
		Iterator<SameLockExclusivePatches> itr = patches.iterator();
		int idx = 0;
		while (itr.hasNext())
		{
			idx++;
			boolean ophandled = false;
			SameLockExclusivePatches op = itr.next();
			Iterator<SameLockExclusivePatches> iitr = patches.iterator();
			int iidx = 0;
			while (iitr.hasNext())
			{
				iidx++;
				SameLockExclusivePatches oop = iitr.next();
				if (iidx <= idx)
				{
					continue;
				}
				SameLockExclusivePatches mres = op.Merge(oop);
				if (mres == null)
				{
					res.add(oop);
				}
				else
				{
					ophandled = true;
					res.add(mres);
				}
			}
			if (!ophandled)
			{
				res.add(op);
			}
		}
		this.patches = res;
		this.patches = OneListMerge();
	}
	
	private List<SameLockExclusivePatches> OneListMerge() throws Exception
	{
		List<Mergeable<SameLockExclusivePatches>> tempinput = new LinkedList<Mergeable<SameLockExclusivePatches>>();
		Iterator<SameLockExclusivePatches> itr2 = patches.iterator();
		while (itr2.hasNext())
		{
			tempinput.add(itr2.next());
		}
		LinkedList<SameLockExclusivePatches> realout = new LinkedList<SameLockExclusivePatches>();
		List<Mergeable<SameLockExclusivePatches>> tempout = new MergeUtil<SameLockExclusivePatches>().Merge(tempinput);
		Iterator<Mergeable<SameLockExclusivePatches>> itr = tempout.iterator();
		while (itr.hasNext())
		{
			realout.add((SameLockExclusivePatches) itr.next());
		}
		return realout;
	}

	public int getSize() {
		return patches.size();
	}
	
}
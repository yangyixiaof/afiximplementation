package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExclusivePatchesManager {
	
	List<SameLockExclusivePatches> patches = new LinkedList<SameLockExclusivePatches>();
	
	public void AddOneExclusivePatch(SameLockExclusivePatches slep)
	{
		patches.add(slep);
	}
	
	public List<SameLockExclusivePatches> MergeSelf() throws Exception
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
	
}
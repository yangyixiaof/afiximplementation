package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SameLockExclusivePatches {
	
	List<OnePatch> patches = new LinkedList<OnePatch>();
	
	public SameLockExclusivePatches() {
	}
	
	public void AddPatches(OnePatch op)
	{
		patches.add(op);
	}
	
	public Iterator<OnePatch> GetIterator()
	{
		return patches.iterator();
	}
	
	/**
	 * 
	 * @param slep
	 * @return true means has merged; false means can not merge.
	 */
	public boolean Merge(SameLockExclusivePatches slep)
	{
		boolean intersected = false;
		Iterator<OnePatch> itr = patches.iterator();
		while (itr.hasNext())
		{
			OnePatch op = itr.next();
			Iterator<OnePatch> iitr = slep.patches.iterator();
			while (iitr.hasNext())
			{
				OnePatch iop = iitr.next();
				boolean ifmerged = op.JudgeIntersectAndMerge(iop);
				if (ifmerged)
				{
					intersected = true;
				}
			}
		}
		return intersected;
	}
	
}

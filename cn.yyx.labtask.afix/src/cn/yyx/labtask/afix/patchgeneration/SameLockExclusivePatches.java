package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ibm.wala.shrikeCT.InvalidClassFileException;

public class SameLockExclusivePatches implements Mergeable<SameLockExclusivePatches>{
	
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
	 * @throws InvalidClassFileException 
	 */
	@Override
	public SameLockExclusivePatches Merge(SameLockExclusivePatches slep) throws Exception
	{
		List<OnePatch> mergeresult = new LinkedList<OnePatch>();
		boolean intersected = false;
		Iterator<OnePatch> itr = patches.iterator();
		while (itr.hasNext())
		{
			OnePatch op = itr.next();
			Iterator<OnePatch> iitr = slep.patches.iterator();
			while (iitr.hasNext())
			{
				OnePatch iop = iitr.next();
				OnePatch tempmergere = op.Merge(iop);
				if (tempmergere != null)
				{
					// means intersected.
					intersected = true;
					mergeresult.add(tempmergere);
				}
				else
				{
					mergeresult.add(op);
					mergeresult.add(iop);
				}
			}
		}
		if (intersected)
		{
			this.patches = OneListMerge(mergeresult);
			return this;
		}
		return null;
	}
	
	private List<OnePatch> OneListMerge(List<OnePatch> tomergelist) throws Exception
	{
		List<Mergeable<OnePatch>> tempinput = new LinkedList<Mergeable<OnePatch>>();
		Iterator<OnePatch> itr2 = tomergelist.iterator();
		while (itr2.hasNext())
		{
			tempinput.add((Mergeable<OnePatch>) itr2.next());
		}
		LinkedList<OnePatch> realout = new LinkedList<OnePatch>();
		List<Mergeable<OnePatch>> tempout = new MergeUtil<OnePatch>().Merge(tempinput);
		Iterator<Mergeable<OnePatch>> itr = tempout.iterator();
		while (itr.hasNext())
		{
			realout.add((OnePatch) itr.next());
		}
		return realout;
	}
	
}
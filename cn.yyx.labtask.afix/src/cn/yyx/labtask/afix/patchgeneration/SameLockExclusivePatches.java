package cn.yyx.labtask.afix.patchgeneration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ibm.wala.shrikeCT.InvalidClassFileException;

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
	 * @throws InvalidClassFileException 
	 */
	public boolean Merge(SameLockExclusivePatches slep) throws InvalidClassFileException
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
				OnePatch tempmergere = op.JudgeIntersectAndMerge(iop);
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
		}
		return intersected;
	}
	
	private List<OnePatch> OneListMerge(List<OnePatch> tomergelist) throws InvalidClassFileException
	{
		boolean intersected = true;
		List<OnePatch> temp = null;
		List<OnePatch> tempres = tomergelist;
		while (intersected)
		{
			intersected = false;
			temp = tempres;
			tempres = new LinkedList<OnePatch>(); 
			Iterator<OnePatch> itr = temp.iterator();
			int l1 = 0;
			while (itr.hasNext())
			{
				l1++;
				int l2 = 0;
				OnePatch op = itr.next();
				Iterator<OnePatch> itr2 = temp.iterator();
				while (itr2.hasNext())
				{
					l2++;
					if (l2 < l1)
					{
						continue;
					}
					OnePatch op2 = itr2.next();
					if (op != op2)
					{
						OnePatch tempmergere = op.JudgeIntersectAndMerge(op2);
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
package cn.yyx.labtask.afix.raceinput;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import cn.yyx.labtask.afix.errordetection.ErrorLocation;
import cn.yyx.labtask.afix.errordetection.ErrorTrace;
import cn.yyx.labtask.afix.errordetection.OneErrorInfo;

public class PCRPool {
	
	TreeMap<String, TreeMap<String, Boolean>> pcrs = new TreeMap<String, TreeMap<String, Boolean>>();
	
	public void AddRacePair(String one, String two)
	{
		AddOneDirection(one, two);
		AddOneDirection(two, one);
	}
	
	private void AddOneDirection(String one, String two)
	{
		TreeMap<String, Boolean> o1 = pcrs.get(one);
		if (o1 == null)
		{
			o1 = new TreeMap<String, Boolean>();
			pcrs.put(one, o1);
		}
		o1.put(two, true);
	}

	public List<OneErrorInfo> GetTraces() {
		List<OneErrorInfo> oeilist = new LinkedList<OneErrorInfo>();
		Set<String> ks = pcrs.keySet();
		Iterator<String> ritr = ks.iterator();
		while (ritr.hasNext())
		{
			String rinfo = ritr.next();
			String[] rs = rinfo.split("#");
			ErrorTrace r = new ErrorTrace();
			r.AddLocationAtPositiveOrder(new ErrorLocation(rs[0], Integer.parseInt(rs[3])));
			
			TreeMap<String, Boolean> pcs = pcrs.get(rinfo);
			Set<String> pcks = pcs.keySet();
			int idx = 0;
			Iterator<String> pck1 = pcks.iterator();
			while (pck1.hasNext())
			{
				idx++;
				String pcrs = pck1.next();
				Iterator<String> pck2 = pcks.iterator();
				int id2 = 0;
				while (id2 <= idx && pck2.hasNext())
				{
					id2++;
					pck2.next();
				}
				while (pck2.hasNext())
				{
					String pcrs2 = pck2.next();
					String[] ps = pcrs.split("#");
					ErrorTrace p = new ErrorTrace();
					p.AddLocationAtPositiveOrder(new ErrorLocation(ps[0], Integer.parseInt(ps[3])));
					String[] cs = pcrs2.split("#");
					ErrorTrace c = new ErrorTrace();
					c.AddLocationAtPositiveOrder(new ErrorLocation(cs[0], Integer.parseInt(cs[3])));
					oeilist.add(new OneErrorInfo(p, c, r));
				}
			}
		}
		return oeilist;
	}
	
}
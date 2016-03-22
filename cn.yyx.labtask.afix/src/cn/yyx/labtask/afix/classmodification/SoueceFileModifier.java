package cn.yyx.labtask.afix.classmodification;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import cn.yyx.labtask.afix.commonutil.FileUtil;
import cn.yyx.labtask.afix.patchgeneration.ExclusivePatchesManager;
import cn.yyx.labtask.afix.patchgeneration.OnePatch;
import cn.yyx.labtask.afix.patchgeneration.SameLockExclusivePatches;

public class SoueceFileModifier {
	
	String sourcefolder = null;
	Map<String, File> allfiles = new TreeMap<String, File>();
	
	public SoueceFileModifier(String sourcefolder) {
		FileUtil.GetAllFilesInADirectory(new File(sourcefolder), allfiles);
	}
	
	public void HandleExclusivePatchesManager(ExclusivePatchesManager epm)
	{
		Iterator<SameLockExclusivePatches> itr = epm.Iterator();
		int lockidx = 0;
		while (itr.hasNext()) {
			lockidx++;
			String lockname = "lock" + lockidx;
			SameLockExclusivePatches slep = itr.next();
			Iterator<OnePatch> opitr = slep.GetIterator();
			while (opitr.hasNext()) {
				OnePatch op = opitr.next();
				String msig = op.getMethodsig();
				
				// TODO
			}
		}
	}
	
}
package cn.yyx.labtask.afix.commonutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Map;

public class FileUtil {

	public static void fileChannelCopy(File source, File dest) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(source);
			fo = new FileOutputStream(dest);
			in = fi.getChannel();
			out = fo.getChannel();
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void GetAllFilesInADirectory(File f, Map<String, File> map) {
		if (f.isDirectory())
		{
			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				GetAllFilesInADirectory(fs[i], map);
			}
		}
		else
		{
			map.put(f.getAbsolutePath(), f);
		}
	}

}
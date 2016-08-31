package cn.yyx.labtask.afix.commonutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.yyx.labtask.afix.classmodification.ASTHelper;

public class FileUtil {
	
	public static void ContentToFile(File fest, String content) throws Exception
	{
		FileWriter fw = new FileWriter(fest.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
	}
	
	public static void FileChannelCopy(File source, File dest) {
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
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				GetAllFilesInADirectory(fs[i], map);
			}
		} else {
			if (f.getAbsolutePath().endsWith(".java"))
			{
				CompilationUnit cu = ASTHelper.GetCompilationUnit(f);
				PackageDeclaration packetDec = cu.getPackage();
				String packinfo = packetDec.getName().toString();
				@SuppressWarnings("unchecked")
				List<ASTNode> types = cu.types();
				Iterator<ASTNode> titr = types.iterator();
				while (titr.hasNext())
				{
					ASTNode astnode = titr.next();
					if (astnode instanceof TypeDeclaration)
					{
						TypeDeclaration typeDec = (TypeDeclaration)astnode;
						String fullname = packinfo + "." + typeDec.getName().toString();
						
						// testing
						// System.err.println("test java file's class full name :" + fullname);
						
						map.put(fullname, f);
					}
				}
			}
		}
	}

	public static String ReadFileByLines(File file) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder("");
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString+"\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}
	
	public static void ClearAndWriteToFile(String content, File file)
	{
		try {
			IFile ifile = FileToIFile(file);
			InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
			ifile.setContents(is, IResource.KEEP_HISTORY, null);
			
			// Common write file.
			/*FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();*/
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public static IFile FileToIFile(File file)
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(file.getAbsolutePath());
		IFile ifile = workspace.getRoot().getFileForLocation(location);
		return ifile;
	}
	
	public static int GetTotalOffsetOfLineEnd(int soff, int line, String content)
	{
		// line start from 0.
		int totaloffset = 0;
		try {
			BufferedReader br = new BufferedReader(new StringReader(content));
			String oneline = null;
			int ldx = 0;
			while (((oneline = br.readLine()) != null) && (ldx <= line))
			{
				ldx++;
				totaloffset += oneline.length() + 1;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return totaloffset - soff - 1;
	}
	
}
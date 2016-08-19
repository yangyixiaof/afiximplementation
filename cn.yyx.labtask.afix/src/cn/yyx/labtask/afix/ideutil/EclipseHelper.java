package cn.yyx.labtask.afix.ideutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import cn.yyx.labtask.afix.Activator;

public class EclipseHelper {

	public static IJavaProject GetSpecifiedProject(String projectname) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			String pname = project.getName();
			if (pname.endsWith(projectname)) {
				try {
					project.open(null);
				} catch (CoreException e) {
					System.err.println("Project open false.");
					e.printStackTrace();
					System.exit(1);
				}
				IJavaProject javaProject = JavaCore.create(project);
				return javaProject;
			}
		}
		return null;
	}

	public static String GetContentOfAResource(String relativepath) {
		StringBuilder sb = new StringBuilder("");
		try {
			// "RaceReport/report_demo_Example"
			URL url = new URL("platform:/plugin/" + Activator.PLUGIN_ID + "/" + relativepath);
			InputStream inputStream = url.openConnection().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine + "\n");
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/*public static void OpenJavaFileAndCursorToSpecificLine(String filepath, int line) {
		IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		if (page != null) {
			IEditorPart editor = page.getActiveEditor();
			if (editor != null) {
				IEditorInput input = editor.getEditorInput();
				if (input instanceof IFileEditorInput) {
					String fileLocation = ((IFileEditorInput) input).getFile().getLocation().toOSString();
					String newFileLocartion = generateNewFileLocation(fileLocation);
					File file = new File(newFileLocartion);
					IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
					try {
						IDE.openEditorOnFileStore(page, fileStore);
					} catch (PartInitException e) {
						// TODO error handling
					}
				}
			}
		}
	}*/

	public static void NavigateToLine(IFile file, Integer line) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(IMarker.LINE_NUMBER, line);
		IMarker marker = null;
		try {
			marker = file.createMarker(IMarker.TEXT);
			marker.setAttributes(map);
			try {
				IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), marker);
			} catch (PartInitException e) {
				// complain
				e.printStackTrace();
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (marker != null)
					marker.delete();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

}
package cn.yyx.labtask.afix.irengine;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class IRGenerator {

	// public static String projectname = null;

	private static IJavaProject javaproject = null;
	private static List<String> libs = new LinkedList<String>();
	private static String libleadprojectname = null;

	public static void InitialLibs(String projectname) {
		synchronized (haha){synchronized (haha){if (libleadprojectname == null || !libleadprojectname.equals(projectname)){IProject[] projects=ResourcesPlugin.getWorkspace().getRoot().getProjects();for (IProject project:projects){if (project.getName().equals(projectname)){javaproject=JavaCore.create(project);libs.clear();IClasspathEntry[] resolvedClasspath=null;try {resolvedClasspath=javaproject.getResolvedClasspath(true);} catch (JavaModelException e){e.printStackTrace();System.exit(1);}for (IClasspathEntry classpathEntry:resolvedClasspath){System.err.println(classpathEntry.getPath());}libleadprojectname=projectname;}}}}}
	}

}

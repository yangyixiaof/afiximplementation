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
	
	public static void InitialLibs(String projectname)
	{
		// projectname = pjtname;
		if (libleadprojectname == null || !libleadprojectname.equals(projectname))
		{
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (IProject project : projects) {
				System.err.println("pj name:" + project.getName());
				if (project.getName().equals(projectname))
				{
					/*try {
						project.open(null); // IProgressMonitor
					} catch (CoreException e) {
						System.err.println("Project open false.");
						e.printStackTrace();
						System.exit(1);
					}*/
					javaproject = JavaCore.create(project);
					libs.clear();
					IClasspathEntry[] resolvedClasspath = null;
					try {
						resolvedClasspath = javaproject.getResolvedClasspath(true);
					} catch (JavaModelException e) {
						e.printStackTrace();
						System.exit(1);
					}
					for (IClasspathEntry classpathEntry : resolvedClasspath) {
						System.err.println(classpathEntry.getPath());
					    // urls.add(classpathEntry.getPath().makeAbsolute().toFile().getCanonicalFile().toURL());
					}
					libleadprojectname = projectname;
				}
			}
		}
	}
	
}
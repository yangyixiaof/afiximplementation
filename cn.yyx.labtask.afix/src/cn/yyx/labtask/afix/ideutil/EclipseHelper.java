package cn.yyx.labtask.afix.ideutil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class EclipseHelper {
	
	public static IJavaProject GetSpecifiedProject(String projectname) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			String pname = project.getName();
			if (pname.endsWith(projectname))
			{
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
	
}
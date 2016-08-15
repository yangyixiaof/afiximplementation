package cn.yyx.labtask.afix.ideutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

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
	
}
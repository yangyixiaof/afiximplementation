package cn.yyx.labtask.afix.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import cn.yyx.labtask.afix.FixHandler;
import cn.yyx.labtask.afix.ideutil.EclipseHelper;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AFixHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public AFixHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// System.err.println("testcontent:" + testcontent);
		// System.err.println("==================== split line in legend hahaha
		// ====================");
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event));
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Start Task", 100);
					// example code.
					/*for (int i = 0; i < 100; i++) {
						if (monitor.isCanceled()) {
							break;
						}
						TimeUnit.MILLISECONDS.sleep(200L);
						monitor.subTask("Start sub task: " + i);
						monitor.worked(1);
					}*/
					
					// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Authenticate"),
					//		"Demo_Authenticate", "demo.Authenticate", monitor);
					// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example2"),
					//		"Demo_Example2", "demo.Example2", monitor);
					FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example"),
							"Demo_Example", "demo.Example", monitor);
					monitor.done();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "Afix", "The process has been run over.");
		return null;
	}
}
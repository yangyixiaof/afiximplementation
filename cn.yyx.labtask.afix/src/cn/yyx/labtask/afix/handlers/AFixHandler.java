package cn.yyx.labtask.afix.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
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

		FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Authenticate"),
				"Demo_Authenticate", "demo.Authenticate");
		// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example2"),
		// 		"Demo_Example2", "demo.Example2");
		// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example"),
		//		"Demo_Example", "demo.Example");

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "Afix", "The process has been run over.");
		return null;
	}
}
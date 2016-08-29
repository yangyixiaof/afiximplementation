package cn.yyx.labtask.afix.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
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
	
	private static Set<HandlerTreeNode> hantasks1st = new TreeSet<HandlerTreeNode>();
	
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
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event));
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					
					ElementTreeSelectionDialog d = new ElementTreeSelectionDialog(window.getShell(), new LabelProvider(), new TreeNodeContentProvider());
					
					AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Authenticate"), "Demo_Authenticate", "demo.Authenticate");
					AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example2"), "Demo_Example2", "demo.Example2");
					AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example"), "Demo_Example", "demo.Example");
					AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_account_Account"), "Account_Account", "account.Account");
					AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_critical_Critical"), "Critical_Critical", "critical.Critical");
					AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_pingpong_PingPong"), "Pingpong_PingPong", "pingpong.PingPong");
					
					d.setInput(GenerateFirstLevelTreeNodes());// new TreeNode[]{input} 
					d.open();
					int flag = d.open();
					if(flag == Dialog.OK) {
						Object obj = d.getFirstResult();
						HandlerTreeNode tn = (HandlerTreeNode)obj;
						HandlerTask hantask = (HandlerTask) tn.getValue();
						if (hantask instanceof HandlerStringTask) {
							HandlerStringTask strhantask = (HandlerStringTask)hantask;
							FixHandler.HandleRaceReport(strhantask.getContent(), strhantask.getProjectname(), strhantask.getMainclass(), monitor);
							
						} else {
							HandlerFileTask filehantask = (HandlerFileTask)hantask;
							FixHandler.HandleRaceReport(filehantask.getReport_file(), filehantask.getProjectname(), filehantask.getMainclass(), monitor);
						}
						System.err.println();
					} else {
						return;
					}
					
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
					// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example"),
					//		"Demo_Example", "demo.Example", monitor);
					// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_account_Account"),
					//		"Account_Account", "account.Account", monitor);
					// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_critical_Critical"),
					//		"Critical_Critical", "critical.Critical", monitor);
					// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_pingpong_PingPong"),
					//		"Pingpong_PingPong", "pingpong.PingPong", monitor);
					monitor.done();
					
					DeleteOneWholeRace("Demo_Authenticate", "demo.Authenticate");
					DeleteOneWholeRace("Demo_Example2", "demo.Example2");
					DeleteOneWholeRace("Demo_Example", "demo.Example");
					DeleteOneWholeRace("Account_Account", "account.Account");
					DeleteOneWholeRace("Critical_Critical", "critical.Critical");
					DeleteOneWholeRace("Pingpong_PingPong", "pingpong.PingPong");
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		MessageDialog.openInformation(window.getShell(), "Afix", "The process has been run over.");
		return null;
	}
	
	private static void AddOneWholeRace(String content, String projectname, String mainclass)
	{
		// TODO
		HandlerTreeNode input=new HandlerTreeNode("root");
		HandlerTreeNode node1=new HandlerTreeNode("node1");
		HandlerTreeNode node2=new HandlerTreeNode("node2");
		input.setChildren(new TreeNode[]{node1,node2});
	}
	
	private static void DeleteOneWholeRace(String projectname, String mainclass)
	{
		// TODO
	}
	
	private static TreeNode[] GenerateFirstLevelTreeNodes()
	{
		return (TreeNode[]) hantasks1st.toArray();
	}
	
}
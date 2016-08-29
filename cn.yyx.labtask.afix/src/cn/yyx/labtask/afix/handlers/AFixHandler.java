package cn.yyx.labtask.afix.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
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

		ElementTreeSelectionDialog d = new ElementTreeSelectionDialog(window.getShell(), new LabelProvider(),
				new TreeNodeContentProvider());

		AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Authenticate"), "Demo_Authenticate",
				"demo.Authenticate");
		AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example2"), "Demo_Example2",
				"demo.Example2");
		AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example"), "Demo_Example",
				"demo.Example");
		AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_account_Account"), "Account_Account",
				"account.Account");
		AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_critical_Critical"), "Critical_Critical",
				"critical.Critical");
		AddOneWholeRace(EclipseHelper.GetContentOfAResource("RaceReport/report_pingpong_PingPong"), "Pingpong_PingPong",
				"pingpong.PingPong");
		
		System.err.println("Whole race size:" + hantasks1st.size());
		
		d.setInput(GenerateFirstLevelTreeNodes());// new TreeNode[]{input}
		int flag = d.open();
		if (flag == Dialog.OK) {
			Object obj = d.getFirstResult();
			HandlerTreeNode tn = (HandlerTreeNode) obj;
			
			if (tn != null && !tn.hasChildren()) {
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event));
				try {
					dialog.run(true, true, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException, InterruptedException {
							HandlerTask hantask = (HandlerTask) tn.getValue();
							monitor.beginTask("Start Task", 100);
							if (hantask instanceof HandlerStringTask) {
								HandlerStringTask strhantask = (HandlerStringTask) hantask;
								FixHandler.HandleRaceReport(strhantask.getContent(), strhantask.getProjectname(),
										strhantask.getMainclass(), monitor);
							} else {
								HandlerFileTask filehantask = (HandlerFileTask) hantask;
								FixHandler.HandleRaceReport(filehantask.getReport_file(), filehantask.getProjectname(),
										filehantask.getMainclass(), monitor);
							}
							monitor.done();
						}
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MessageDialog.openInformation(window.getShell(), "Afix", "The process has been run over.");
			} else {
				MessageDialog.openInformation(window.getShell(), "Afix", "Invalid selection, the leaf tree node must be selected.");
			}
		}
		// example code.
		// for (int i = 0; i < 100; i++) {
		// if (monitor.isCanceled()) {
		// break;
		// }
		// TimeUnit.MILLISECONDS.sleep(200L);
		// monitor.subTask("Start sub task: " + i);
		// monitor.worked(1);
		// }

		// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Authenticate"),
		// "Demo_Authenticate", "demo.Authenticate", monitor);
		// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example2"),
		// "Demo_Example2", "demo.Example2", monitor);
		// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_demo_Example"),
		// "Demo_Example", "demo.Example", monitor);
		// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_account_Account"),
		// "Account_Account", "account.Account", monitor);
		// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_critical_Critical"),
		// "Critical_Critical", "critical.Critical", monitor);
		// FixHandler.HandleRaceReport(EclipseHelper.GetContentOfAResource("RaceReport/report_pingpong_PingPong"),
		// "Pingpong_PingPong", "pingpong.PingPong", monitor);

		DeleteOneWholeRace("Demo_Authenticate", "demo.Authenticate");
		DeleteOneWholeRace("Demo_Example2", "demo.Example2");
		DeleteOneWholeRace("Demo_Example", "demo.Example");
		DeleteOneWholeRace("Account_Account", "account.Account");
		DeleteOneWholeRace("Critical_Critical", "critical.Critical");
		DeleteOneWholeRace("Pingpong_PingPong", "pingpong.PingPong");

		return null;
	}

	private static void AddOneWholeRace(String content, String projectname, String mainclass) {
		boolean find = false;
		HandlerTreeNode htn = null;
		Iterator<HandlerTreeNode> hitr = hantasks1st.iterator();
		while (hitr.hasNext()) {
			htn = hitr.next();
			HandlerTask hantask = (HandlerTask) htn.getValue();
			if (hantask.getProjectname().equals(projectname)) {
				find = true;
				break;
			}
		}
		if (find) {
			int findidx = -1;
			TreeNode[] childs = htn.getChildren();
			for (int i = 0; i < childs.length; i++) {
				TreeNode tn = childs[i];
				HandlerTreeNode chtn = (HandlerTreeNode) tn;
				HandlerTask hantask = (HandlerTask) chtn.getValue();
				if (hantask.getMainclass().equals(mainclass)) {
					findidx = i;
					break;
				}
			}
			if (findidx >= 0) {
				childs[findidx] = new HandlerTreeNode(new HandlerStringTask(content, projectname, mainclass));
			} else {
				TreeNode[] newchilds = new HandlerTreeNode[childs.length + 1];
				for (int i = 0; i < childs.length; i++) {
					newchilds[i] = childs[i];
				}
				newchilds[childs.length] = new HandlerTreeNode(new HandlerStringTask(content, projectname, mainclass));
			}
		} else {
			HandlerTreeNode nhtn1st = new HandlerTreeNode(new HandlerStringTask(content, projectname, projectname));
			HandlerTreeNode nhtn2nd = new HandlerTreeNode(new HandlerStringTask(content, projectname, mainclass));
			nhtn1st.setChildren(new TreeNode[] { nhtn2nd });
			hantasks1st.add(nhtn1st);
		}
	}

	private static void DeleteOneWholeRace(String projectname, String mainclass) {
		boolean find1st = false;
		boolean find2nd = false;
		HandlerTreeNode htn = null;
		Iterator<HandlerTreeNode> hitr = hantasks1st.iterator();
		while (hitr.hasNext()) {
			htn = hitr.next();
			HandlerTask hantask = (HandlerTask) htn.getValue();
			if (hantask.getProjectname().equals(projectname)) {
				find1st = true;
				break;
			}
		}
		if (find1st) {
			int removedidx = -1;
			TreeNode[] childs = htn.getChildren();
			for (int i = 0; i < childs.length; i++) {
				TreeNode tn = childs[i];
				HandlerTreeNode chtn = (HandlerTreeNode) tn;
				HandlerTask hantask = (HandlerTask) chtn.getValue();
				if (hantask.getMainclass().equals(mainclass)) {
					find2nd = true;
					removedidx = i;
					break;
				}
			}
			if (removedidx >= 0) {
				if (childs.length <= 1) {
					// delete htn itself.
					hantasks1st.remove(htn);
				} else {
					int idx = 0;
					TreeNode[] newchilds = new HandlerTreeNode[childs.length - 1];
					for (int i = 0; i < childs.length; i++) {
						if (i != removedidx) {
							newchilds[idx] = childs[i];
							idx++;
						}
					}
					htn.setChildren(newchilds);
				}
			}
		}
		if (!find2nd) {
			System.err.println("projectname:" + projectname + ";mainclass:" + mainclass + ", can not be found.");
		}
	}

	private static TreeNode[] GenerateFirstLevelTreeNodes() {
		TreeNode[] tns = new HandlerTreeNode[hantasks1st.size()];
		Iterator<HandlerTreeNode> itr = hantasks1st.iterator();
		int idx = 0;
		while (itr.hasNext()) {
			HandlerTreeNode htn = itr.next();
			tns[idx] = htn;
			idx++;
		}
		return tns;
	}

}
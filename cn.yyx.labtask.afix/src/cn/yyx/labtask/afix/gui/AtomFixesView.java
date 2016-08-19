package cn.yyx.labtask.afix.gui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import cn.yyx.labtask.afix.irengine.IRGenerator;

public class AtomFixesView extends ViewPart {
	
	TableColumn typeCol = null;
	TableColumn nameCol = null;
	TableColumn opCol = null;
	TableColumn locCol = null;
	
	private static TableViewer viewer = null;
	
	@Override
	public void createPartControl(Composite parent) {
		setViewer(new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION));
		final Table table = getViewer().getTable();
		
		typeCol = new TableColumn(table, SWT.LEFT);
		typeCol.setText("");
		typeCol.setWidth(18);
		
		nameCol = new TableColumn(table, SWT.LEFT);
		nameCol.setText("LockName");
		nameCol.setWidth(175);
		
		opCol = new TableColumn(table, SWT.LEFT);
		opCol.setText("OperationType");
		opCol.setWidth(175);
		
		locCol = new TableColumn(table, SWT.LEFT);
		locCol.setText("Location");
		locCol.setWidth(300);
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		getViewer().setContentProvider(new TableViewerContentProvider());
		getViewer().setLabelProvider(new TableViewerLabelProvider());
		getViewer().setInput(AFixFactory.list);
		
		// testing
		IRGenerator.InitialLibs("classpathtest");
	}

	@Override
	public void setFocus() {
		getViewer().getControl().setFocus();
	}

	public static TableViewer getViewer() {
		return viewer;
	}

	public static void setViewer(TableViewer viewer) {
		AtomFixesView.viewer = viewer;
	}
	
	public static void RefreshViewer()
	{
		if (viewer != null)
		{
			viewer.refresh();
		}
	}
	
}
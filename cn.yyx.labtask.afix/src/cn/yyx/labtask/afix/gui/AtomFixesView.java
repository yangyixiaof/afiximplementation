package cn.yyx.labtask.afix.gui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

public class AtomFixesView extends ViewPart {
	
	TableColumn typeCol = null;
	TableColumn nameCol = null;
	TableColumn locCol = null;
	
	TableViewer viewer = null;
	
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();
		
		typeCol = new TableColumn(table, SWT.LEFT);
		typeCol.setText("");
		typeCol.setWidth(18);
		
		nameCol = new TableColumn(table, SWT.LEFT);
		nameCol.setText("LockName");
		nameCol.setWidth(200);
		
		locCol = new TableColumn(table, SWT.LEFT);
		locCol.setText("Location");
		locCol.setWidth(450);
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		viewer.setContentProvider(new TableViewerContentProvider());
		viewer.setLabelProvider(new TableViewerLabelProvider());
		viewer.setInput(AFixFactory.list);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
}
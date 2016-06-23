package cn.yyx.labtask.afix.gui;

import java.io.File;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableViewerLabelProvider implements ITableLabelProvider {

	{
		File directory = new File("");
		try {
			System.out.println(directory.getCanonicalPath());
			System.out.println(directory.getAbsolutePath());
		} catch (Exception e) {
		}
	}

	Image image = new Image(null, "icons/atomfixicon.png");

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
		image.dispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return image;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		AFixEntity o = (AFixEntity) element;
		if (columnIndex == 0) {
			return "";
		}
		if (columnIndex == 1) {
			return o.getLockname();
		}
		if (columnIndex == 2) {
			return o.getLocklocation();
		}
		return null;
	}

}

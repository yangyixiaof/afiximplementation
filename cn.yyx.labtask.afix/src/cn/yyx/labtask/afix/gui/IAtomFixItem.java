package cn.yyx.labtask.afix.gui;

import org.eclipse.core.runtime.IAdaptable;

public interface IAtomFixItem extends IAdaptable {
	
	String getName();
	void setName(String newname);
	String getLocation();
	boolean isFavoriteFor(Object obj);
	AtomFixItemSorter getSorter();
	String getInfo();
	
	static IAtomFixItem[] NONE = new IAtomFixItem[]{};
}
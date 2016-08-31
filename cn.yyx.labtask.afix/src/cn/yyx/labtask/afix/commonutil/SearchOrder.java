package cn.yyx.labtask.afix.commonutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.ibm.wala.types.TypeName;
import com.ibm.wala.util.strings.StringStuff;

import attrib4j.bcel.DescriptorUtil;

public class SearchOrder {
	
	private ArrayList<String> classlist = new ArrayList<String>();
	private String methodreturntype = null;
	private ArrayList<String> methodparam = new ArrayList<String>();
	
	Map<TypeDeclaration, Boolean> classidxincreased = new HashMap<TypeDeclaration, Boolean>();
	int classidx = 0;
	boolean isrightclass = false;
	
	public SearchOrder(String msig) {
		int ll = msig.indexOf('(');
		String type = msig.substring(0, ll);
		int rd = type.lastIndexOf('.');
		type = type.substring(0, rd);
		String[] tps = type.split("\\$");
		int tp0dx = tps[0].lastIndexOf('.');
		tps[0] = tps[0].substring(tp0dx+1);
		classlist.addAll(Arrays.asList(tps));
		String methoddescriptor = msig.substring(ll);
		TypeName tn = StringStuff.parseForReturnTypeName(methoddescriptor);
		String rt = tn.toString();
		// System.out.println("rt:"+rt);
		String[] res = DescriptorUtil.convertToJavaFormat(methoddescriptor);
		methodreturntype = DescriptorUtil.convertToJavaFormat("("+rt+")V")[0];
		methodparam.addAll(Arrays.asList(res));
		/*for (int i=0;i<res.length;i++)
		{
			System.out.println("result:" + res[i]);
		}
		System.out.println("methodreturntype:"+methodreturntype);
		for (int j=0;j<classlist.size();j++)
		{
			System.out.println("class:" + classlist.get(j));
		}*/
		
		// debugging.
		// System.err.println("test begin.");
		// System.err.println("msig:" + msig);
		// System.err.println("classes:" + classlist);
		// System.err.println("test end.");
	}
	
	/*public static void main(String[] args) {
		new SearchOrder("demo.Example$MyThread.run()V");
	}*/

	public ArrayList<String> getClasslist() {
		return classlist;
	}

	public void setClasslist(ArrayList<String> classlist) {
		this.classlist = classlist;
	}

	public String getMethodreturntype() {
		return methodreturntype;
	}

	public void setMethodreturntype(String methodreturntype) {
		this.methodreturntype = methodreturntype;
	}

	public ArrayList<String> getMethodparam() {
		return methodparam;
	}

	public void setMethodparam(ArrayList<String> methodparam) {
		this.methodparam = methodparam;
	}

	public boolean HandleCurrentClass(TypeDeclaration node) {
		String rawclass = node.getName().toString();
		// testing
		System.out.println("rawclass:"+rawclass+";classidx:"+classidx+";classlistsize:"+classlist.size());
		
		isrightclass = false;
		if (classidx >= classlist.size())
		{
			return false;
		}
		String classname = classlist.get(classidx);
		
		// testing
		System.out.println("classname:"+classname+";rawclass:"+rawclass+".");
		
		if (classname.endsWith(rawclass))
		{
			classidx++;
			classidxincreased.put(node, true);
			isrightclass = true;
			return true;
		}
		return false;
	}
	
	public void DeHandleCurrentClass(TypeDeclaration node)
	{
		if (classidxincreased.get(node) != null && classidxincreased.get(node) == true)
		{
			classidx--;
		}
		isrightclass = false;
	}

	public boolean IsInRightClass() {
		if ((classidx == classlist.size()) && isrightclass)
		{
			return true;
		}
		return false;
	}

	public boolean IsInRightMethod(Type tp, List<SingleVariableDeclaration> params) {
		
		// testing
		System.out.println("tp:"+tp+";methodreturntype:"+methodreturntype+";params size:"+params.size()+";methodparam:"+methodparam.size());
		
		if (methodparam.size() != params.size())
		{
			return false;
		}
		if (TypeUtil.TypeComparable(tp, methodreturntype))
		{
			Iterator<SingleVariableDeclaration> itr = params.iterator();
			Iterator<String> mitr = methodparam.iterator();
			while (itr.hasNext())
			{
				SingleVariableDeclaration svd = itr.next();
				String comptype = mitr.next();
				Type pt = svd.getType();
				if (!TypeUtil.TypeComparable(pt, comptype))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
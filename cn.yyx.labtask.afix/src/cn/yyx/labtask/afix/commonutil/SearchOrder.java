package cn.yyx.labtask.afix.commonutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.ibm.wala.types.TypeName;
import com.ibm.wala.util.strings.StringStuff;

import attrib4j.bcel.DescriptorUtil;

public class SearchOrder {
	
	private ArrayList<String> classlist = new ArrayList<String>();
	private String methodreturntype = null;
	private ArrayList<String> methodparam = new ArrayList<String>();
	
	int classidx = 0;
	
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

	public boolean HandleCurrentClass(String rawclass) {
		String classname = classlist.get(classidx);
		if (classname.endsWith(rawclass))
		{
			classidx++;
			return true;
		}
		return false;
	}
	
	public void DecreaseLevel()
	{
		classidx--;
	}

	public boolean IsInRightClass() {
		if (classidx == classlist.size()-1)
		{
			return true;
		}
		return false;
	}

	public boolean IsInRightMethod(Type tp, List<SingleVariableDeclaration> params) {
		
		return false;
	}
	
}
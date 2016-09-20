package cn.yyx.labtask.afix.commonutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.ibm.wala.types.TypeName;
import com.ibm.wala.util.strings.StringStuff;

import attrib4j.bcel.DescriptorUtil;
import cn.yyx.labtask.afix.classmodification.ASTHelper;

public class SearchOrder {
	
	private ArrayList<String> classlist = new ArrayList<String>();
	private String methodreturntype = null;
	private String methodname = null;
	private ArrayList<String> methodparam = new ArrayList<String>();
	private String methodsig = null;
	
	Map<ASTNode, Boolean> classidxincreased = new HashMap<ASTNode, Boolean>();
	int classidx = 0;
	Stack<Boolean> isrightclass = new Stack<Boolean>();
	
	int line = -1;
	
	public SearchOrder(String msig, int line) {
		this.line = line;
		this.setMethodsig(msig);
		int ll = msig.indexOf('(');
		String type = msig.substring(0, ll);
		int rd = type.lastIndexOf('.');
		this.setMethodname(type.substring(rd+1));
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

	public boolean HandleCurrentClass(ASTNode node) {
		String rawclass = null;
		if (node instanceof TypeDeclaration)
		{
			rawclass = ((TypeDeclaration)node).getName().toString();
		}
		// testing
		System.out.println("rawclass:"+rawclass+";classidx:"+classidx+";classlistsize:"+classlist.size());
		
		boolean tempisrightclass = false;
		if (classidx < classlist.size()) {
			String classname = classlist.get(classidx);
			// testing
			System.out.println("classname:"+classname+";rawclass:"+rawclass+".");
			if (classname.endsWith(rawclass) || (rawclass == null && StringAnalysis.IsInteger(classname)))
			{
				classidx++;
				classidxincreased.put(node, true);
				tempisrightclass = true;
			}
		}
		isrightclass.push(tempisrightclass);
		return tempisrightclass;
	}
	
	public void DeHandleCurrentClass(ASTNode node)
	{
		if (classidxincreased.get(node) != null && classidxincreased.get(node) == true)
		{
			classidx--;
		}
		isrightclass.pop();
	}

	public boolean IsInRightClass(ASTNode node, CompilationUnit cu) {
		if ((classidx == classlist.size()) && isrightclass.peek() && line >= ASTHelper.GetASTNodeLineNumber(cu, node) && line <= ASTHelper.GetASTNodeEndLineNumber(cu, node))
		{
			return true;
		}
		return false;
	}

	public boolean IsInRightMethod(String mname, Type tp, List<SingleVariableDeclaration> params) {
		
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
			if (mname.equals(this.methodname))
			{
				return true;
			}
		}
		return false;
	}

	public String getMethodsig() {
		return methodsig;
	}

	public void setMethodsig(String methodsig) {
		this.methodsig = methodsig;
	}
	
	@Override
	public String toString() {
		return "methodreturntype:" + methodreturntype + ";methodname:" + methodname + ";methodparam:" + methodparam;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	
}
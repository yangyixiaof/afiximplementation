package cn.yyx.labtask.afix.rvparse;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.jdt.core.Signature;

import cn.yyx.labtask.afix.raceinput.PCRPool;
import cn.yyx.labtask.afix.rvparse.RvParser.ClassDeclareContext;

public class RVStructureVisitor extends RvBaseVisitor<Integer> {
	
	private Queue<String> usedobj = new LinkedList<String>();
	PCRPool pp = null;
	
	public RVStructureVisitor(PCRPool pp) {
		this.pp = pp;
	}
	
	@Override
	public Integer visitOneRaceReadPart(RvParser.OneRaceReadPartContext ctx) {
		Integer res = visitChildren(ctx);
		List<ClassDeclareContext> cls = ctx.classDeclare();
		String where = cls.get(0).getText();
		
		// System.err.println("where:"+where);
		
		String rt = ctx.returnType().getText();
		String rtsig = Signature.createTypeSignature(rt, true);
		String methodsig = ctx.methodSig().getText();

		int lp = methodsig.indexOf('(');
		int rp = methodsig.indexOf(')');
		String sig = methodsig.substring(lp+1, rp).trim();
		String met = methodsig.substring(0,lp);
		String msig = "()" + rtsig;
		if (sig.length() > 0)
		{
			String[] ss = sig.split(",");
			String[] nss = new String[ss.length];
			for (int i=0;i<ss.length;i++)
			{
				nss[i] = Signature.createTypeSignature(ss[i], true);
			}
			msig = Signature.createMethodSignature(nss, rtsig); 
		}
		msig = met + msig;
		// System.err.println("methodsig:"+methodsig);
		// String msig = DescriptorUtil.convert(sig, rt);
		
		// System.err.println("msig:"+msig);
		
		String vartype = ctx.variableType().getText();
		String var = ctx.variable().getText();
		String line = ctx.lineNumber().getText();
		String key = where + "." + msig + "#" + vartype + "#" + var + "#" + line;
		usedobj.add(key);
		return res;
	}

	@Override
	public Integer visitOneRaceWritePart(RvParser.OneRaceWritePartContext ctx) {
		Integer res = visitChildren(ctx);
		List<ClassDeclareContext> cls = ctx.classDeclare();
		String where = cls.get(0).getText();
		
		// System.err.println("where:"+where);
		
		String rt = ctx.returnType().getText();
		String rtsig = Signature.createTypeSignature(rt, true);
		String methodsig = ctx.methodSig().getText();

		int lp = methodsig.indexOf('(');
		int rp = methodsig.indexOf(')');
		String sig = methodsig.substring(lp+1, rp).trim();
		String met = methodsig.substring(0,lp);
		String msig = "()" + rtsig;
		if (sig.length() > 0)
		{
			String[] ss = sig.split(",");
			String[] nss = new String[ss.length];
			for (int i=0;i<ss.length;i++)
			{
				nss[i] = Signature.createTypeSignature(ss[i], true);
			}
			msig = Signature.createMethodSignature(nss, rtsig); 
		}
		msig = met + msig;
		// System.err.println("methodsig:"+methodsig);
		// String msig = DescriptorUtil.convert(sig, rt);
		
		// System.err.println("msig:"+msig);
		
		String vartype = ctx.variableType().getText();
		String var = ctx.variable().getText();
		String line = ctx.lineNumber().getText();
		String key = where + "." + msig + "#" + vartype + "#" + var + "#" + line;
		usedobj.add(key);
		return res;
	}

	@Override
	public Integer visitOneRacePart(RvParser.OneRacePartContext ctx) {
		Integer res = visitChildren(ctx);
		return res;
	}

	@Override
	public Integer visitOneRace(RvParser.OneRaceContext ctx) {
		Integer res = visitChildren(ctx);
		String one = usedobj.poll();
		String two = usedobj.poll();
		pp.AddRacePair(one, two);
		return res;
	}

	@Override
	public Integer visitClassDeclare(RvParser.ClassDeclareContext ctx) {
		Integer res = visitChildren(ctx);
		return res;
	}

	@Override
	public Integer visitVariableType(RvParser.VariableTypeContext ctx) {
		Integer res = visitChildren(ctx);
		return res;
	}

	@Override
	public Integer visitVariable(RvParser.VariableContext ctx) {
		Integer res = visitChildren(ctx);
		return res;
	}

	@Override
	public Integer visitReturnType(RvParser.ReturnTypeContext ctx) {
		Integer res = visitChildren(ctx);
		return res;
	}

	@Override
	public Integer visitMethodSig(RvParser.MethodSigContext ctx) {
		Integer res = visitChildren(ctx);
		return res;
	}

	@Override
	public Integer visitLineNumber(RvParser.LineNumberContext ctx) {
		Integer res = visitChildren(ctx);
		return res;
	}
	
}
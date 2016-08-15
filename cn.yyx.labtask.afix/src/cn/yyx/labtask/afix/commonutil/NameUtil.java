package cn.yyx.labtask.afix.commonutil;

public class NameUtil {
	
	public static String GetClassNameFromMethodSig(String msig)
	{
		int bra = msig.indexOf('(');
		String mtype = msig.substring(0, bra);
		int lddx = mtype.lastIndexOf('.');
		mtype = mtype.substring(0, lddx);
		return mtype;
	}
	
	public static String GetNormalClassNameFromMethodSig(String msig)
	{
		int bra = msig.indexOf('(');
		String mtype = msig.substring(0, bra);
		int lddx = mtype.lastIndexOf('.');
		mtype = mtype.substring(0, lddx);
		int ddx = mtype.lastIndexOf('$');
		if (ddx > 0)
		{
			mtype = mtype.substring(0, ddx);
		}
		return mtype;
	}
	
	public static String TranslateJVMNameToUnifiedForm(String jvmname)
	{
		return jvmname.replace('.', '/');
	}
	
}
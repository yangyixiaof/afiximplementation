package cn.yyx.labtask.afix.errordetection;

public class ErrorLocation {
	
	private String sig = null;
	private int bytecodel = -1;
	
	public ErrorLocation(String sig, int bytecodel)
	{
		
		//testing
		System.out.println("methodsig:" + sig + ";line:" + bytecodel);
		
		this.setSig(sig);
		this.setBytecodel(bytecodel);
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public int getBytecodel() {
		return bytecodel;
	}

	public void setBytecodel(int bytecodel) {
		this.bytecodel = bytecodel;
	}
	
	public boolean InSameMethod(ErrorLocation el)
	{
		if (sig.equals(el.sig))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object el) {
		if (el instanceof ErrorLocation)
		{
			if (sig.equals(((ErrorLocation)el).sig) && (bytecodel == ((ErrorLocation)el).bytecodel))
			{
				return true;
			}
		}
		return false;
	}
	
}
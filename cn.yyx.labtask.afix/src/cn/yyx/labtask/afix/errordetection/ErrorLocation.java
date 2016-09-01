package cn.yyx.labtask.afix.errordetection;

public class ErrorLocation {
	
	private String sig = null;
	private int line = -1;
	private String racevar = null;
	
	public ErrorLocation(String sig, int line, String racevar)
	{
		this.setSig(sig);
		this.setLine(line);
		this.setRacevar(racevar);
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int bytecodel) {
		this.line = bytecodel;
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
			if (sig.equals(((ErrorLocation)el).sig) && (line == ((ErrorLocation)el).line))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "methodsig:" + sig + ";line:" + line;
	}

	public String getRacevar() {
		return racevar;
	}

	public void setRacevar(String racevar) {
		this.racevar = racevar;
	}
	
}
package cn.yyx.labtask.afix.patchgeneration;

public class InsertPosition {
	
	private int position = -1;
	private String racevar = null;
	
	public InsertPosition(int position, String racevar) {
		this.position = position;
		this.racevar = racevar;
	}

	public int getLineNumber() {
		return position;
	}

	public void setLineNumber(int position) {
		this.position = position;
	}

	public String getRacevar() {
		return racevar;
	}

	public void setRacevar(String racevar) {
		this.racevar = racevar;
	}
	
}
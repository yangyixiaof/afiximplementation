package cn.yyx.labtask.afix.patchgeneration;

import cn.yyx.labtask.afix.errordetection.ErrorLocation;

public class OnePatchGenerator {
	
	ErrorLocation p = null;
	ErrorLocation c = null;
	ErrorLocation r = null;
	
	public OnePatchGenerator(ErrorLocation p, ErrorLocation c, ErrorLocation r)
	{
		this.p = p;
		this.c = c;
		this.r = r;
	}
	
}
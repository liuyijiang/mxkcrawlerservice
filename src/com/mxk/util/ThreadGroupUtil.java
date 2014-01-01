package com.mxk.util;
/**
 * 集成线程组
 * @author Administrator
 *
 */
public class ThreadGroupUtil extends ThreadGroup {

	public ThreadGroupUtil(String name,boolean isDoeam) {
		super(name);
		this.setDaemon(isDoeam);
	}

	
}

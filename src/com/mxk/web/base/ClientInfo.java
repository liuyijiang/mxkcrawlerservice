package com.mxk.web.base;

public enum ClientInfo {
   
	APP(1,"app"),
	WEB(2,"web");
	
	private String str;
	private int code;
	
	private ClientInfo(int code,String str){
		this.code = code;
		this.str = str;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	
}

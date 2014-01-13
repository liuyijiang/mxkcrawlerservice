package com.mxk.translator;

public enum TranslatorType {

	/**中文简体*/
	CHINIA("zh-CN"),
	
	/**英语*/
	ENGLISH("en"),
	
	/**俄语*/
	RUSSIA("ru"),
	
	/**日语*/
	JAPAN("ja");
	
	private String code;
	
	private TranslatorType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}

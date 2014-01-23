package com.mxk.crawler.base;
/**
 * 
 * @author Administrator
 *
 */
public enum SiteInfo {
   
	EUROPE("欧美网站"),
	
	RUSSIA("俄罗斯网站"),
	
	JAPAN("日本网站"),
	
	SWEDEN("瑞典网站"),
	
	/** 中国 */
	CHINIA_TW("中国台湾网站"),
	
	CHINIA("国内网站");
	
    private String code;
	
	private SiteInfo(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}

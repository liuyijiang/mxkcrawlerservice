package com.mxk.crawler.model;
/**
 * 链接类型
 * @author Administrator
 *
 */
public enum LinkType {
  
	/** 爬取网站的根链接 不需要被修改状态 */
	ROOT_LINK(1,"根链接"),
	
	COMMON_LINK(2,"一般的爬取链接");
	
	private int code;
	private String show;
	
	private LinkType(int code,String show){
		this.code = code;
		this.show = show;
	}

	public int getCode() {
		return code;
	}

	public String getShow() {
		return show;
	}
	
	
}

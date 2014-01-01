package com.mxk.crawler.base;
/**
 * 爬取器状态
 * @author liuyijiang
 *
 */
public enum CrawlerState {

	WAIT("挂起"),RUNNING("执行");
	
	private String code;
	
	private CrawlerState(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	
}

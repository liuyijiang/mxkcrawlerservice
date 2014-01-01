package com.mxk.crawler.annotation;
/**
 * 爬取器爬取资源类型
 * @author Administrator
 *
 */
public enum CrawleType {

	LINK("链接"), CONTENT("帖子");
	
	private String description;
	
	private CrawleType(String description){
	    this.description = description;	
	}

	public String getDescription() {
		return description;
	}
	
}

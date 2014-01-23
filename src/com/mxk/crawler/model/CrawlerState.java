package com.mxk.crawler.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CrawlerState {
   
	@Id
	private String id;
	private String crawlerName;
	private String crawlerSiteName;
	private String crawlerSiteUrl;
	private String lastExecuteTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCrawlerName() {
		return crawlerName;
	}
	public void setCrawlerName(String crawlerName) {
		this.crawlerName = crawlerName;
	}
	public String getCrawlerSiteName() {
		return crawlerSiteName;
	}
	public void setCrawlerSiteName(String crawlerSiteName) {
		this.crawlerSiteName = crawlerSiteName;
	}
	public String getCrawlerSiteUrl() {
		return crawlerSiteUrl;
	}
	public void setCrawlerSiteUrl(String crawlerSiteUrl) {
		this.crawlerSiteUrl = crawlerSiteUrl;
	}
	public String getLastExecuteTime() {
		return lastExecuteTime;
	}
	public void setLastExecuteTime(String lastExecuteTime) {
		this.lastExecuteTime = lastExecuteTime;
	}
	
	
}

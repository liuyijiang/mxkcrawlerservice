package com.mxk.crawler.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 基础爬取链接
 * @author Administrator
 *
 */
@Document
public class Links extends BaseResource{
  
	@Id
	private String id;
	private Date createTime;
	private String url;
	private String fromUrl;
	private int state;
	private String matchUrl;//配的的url
	private String multiData;//多样性 数据 用于保存评论 图片链接地址等等
	private int linkType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFromUrl() {
		return fromUrl;
	}
	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMatchUrl() {
		return matchUrl;
	}
	public void setMatchUrl(String matchUrl) {
		this.matchUrl = matchUrl;
	}
	public String getMultiData() {
		return multiData;
	}
	public void setMultiData(String multiData) {
		this.multiData = multiData;
	}
	public int getLinkType() {
		return linkType;
	}
	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}
	
}

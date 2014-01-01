package com.mxk.crawler.model;

import java.util.List;


/**
 * 内容 指论坛中的帖子
 * @author Administrator
 *
 */
public class Content extends BaseResource{

	private String linkId;//
	private String headline;//标题
	private String owner;//
	private String sitename;
	private String siteurl;
	private String likurl;
	private int state;
	private String comment;
	private String post;
	public List<String> images;
	
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public String getSiteurl() {
		return siteurl;
	}
	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}
	public String getLikurl() {
		return likurl;
	}
	public void setLikurl(String likurl) {
		this.likurl = likurl;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	
    	
}

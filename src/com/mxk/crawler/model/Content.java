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
	private String info;//内容
	private String hit;//阅读次数
	private String post;
	public List<String> images;
	private String simpleImage;
	private String multiData;//额外的一些有价值的数据;
	private String multiImageName;
//	private String untranslateInfo;
	
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getHit() {
		return hit;
	}
	public void setHit(String hit) {
		this.hit = hit;
	}
	public String getSimpleImage() {
		return simpleImage;
	}
	public void setSimpleImage(String simpleImage) {
		this.simpleImage = simpleImage;
	}
	public String getMultiData() {
		return multiData;
	}
	public void setMultiData(String multiData) {
		this.multiData = multiData;
	}
	public String getMultiImageName() {
		return multiImageName;
	}
	public void setMultiImageName(String multiImageName) {
		this.multiImageName = multiImageName;
	}
	
    	
}

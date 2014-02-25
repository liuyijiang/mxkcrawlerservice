package com.mxk.crawler.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 爬取服务整理后的文章资源
 * @author Administrator
 *
 */
@Document
public class ContentResource extends BaseResource {

	@Id
	private String id;
	private String resourceId;//资源id
	private String headline;//标题
	private String owner;//
	private String sitename;
	private String siteurl;
	private String linkurl;
	private int state;//是否被编目
	private String comment;
	private String info;//内容
	private String hit;//阅读次数
	private String post;//评论数量
	private String images;
	public String simpleImage;
	public String simpleImageName;
	public String multiData;//额外的一些有价值的数据;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
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
	public String getSimpleImageName() {
		return simpleImageName;
	}
	public void setSimpleImageName(String simpleImageName) {
		this.simpleImageName = simpleImageName;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	
}

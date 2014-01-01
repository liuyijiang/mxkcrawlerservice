package com.mxk.crawler.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 互联网资源 各个门户爬取后数据整合
 * @author Administrator
 *
 */
@Document
public class WebResource  extends BaseResource {

	@Id
	private String id;
	private String name;
	private String keyword;
	private String lastUpdateTime;
	private long quantity;//资源数量
    private String type;//资源类型 (军舰 ..)
    private Set<String> tag;//标签
    private long read;//阅读
    private long collect;//
    private String img;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Set<String> getTag() {
		return tag;
	}
	public void setTag(Set<String> tag) {
		this.tag = tag;
	}
	public long getRead() {
		return read;
	}
	public void setRead(long read) {
		this.read = read;
	}
	public long getCollect() {
		return collect;
	}
	public void setCollect(long collect) {
		this.collect = collect;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
    
    
    
}

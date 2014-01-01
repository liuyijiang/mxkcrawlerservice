package com.mxk.crawler.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Tag {

	@Id
	private String id;
	private String name;
	private int sort;//排序号
	private Date createTime;
	private String type;//军舰 飞机 坦克
	private List<SubTag> subtags;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public List<SubTag> getSubtags() {
		return subtags;
	}
	public void setSubtags(List<SubTag> subtags) {
		this.subtags = subtags;
	}
	
	
	
}

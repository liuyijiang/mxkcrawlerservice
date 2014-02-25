package com.mxk.crawler.model;

import java.util.Date;

public class UserActionLog {

	private String id;
	private String url; //请求的全路径
	private String parmas;
	private String client;//web app
	private String request;//请求
	private Date createTime;
	private int actionTime;//请求的执行时间 ms
	private String success;
	private String ip;
	private String method;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParmas() {
		return parmas;
	}
	public void setParmas(String parmas) {
		this.parmas = parmas;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getActionTime() {
		return actionTime;
	}
	public void setActionTime(int actionTime) {
		this.actionTime = actionTime;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	
	
	
}

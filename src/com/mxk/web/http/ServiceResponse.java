package com.mxk.web.http;
/**
 * 系统服务器返回值
 * @author Administrator
 *
 */
public enum ServiceResponse {
	
	/***/
	SUCCESS(200,"操作成功"),
	
	/***/
	SERVICE_ERROR(500,"服务器异常"),
	
	/** 数据重复 */
	DATA_REPETITION(50001,"数据重复");
	
	private Integer code;
	private String desc;
	
	private ServiceResponse(Integer code,String desc){
		this.code = code;
		this.desc= desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	
}

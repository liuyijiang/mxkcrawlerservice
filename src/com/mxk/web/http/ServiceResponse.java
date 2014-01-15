package com.mxk.web.http;
/**
 * 系统服务器返回值
 * @author Administrator
 *
 */
public enum ServiceResponse {
	
	/***/
	SUCCESS(200),
	
	/***/
	SERVICE_ERROR(500),
	
	/** 数据重复 */
	DATA_REPETITION(50001);
	
	private Integer code;
	
	private ServiceResponse(Integer code){
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
	
	
}

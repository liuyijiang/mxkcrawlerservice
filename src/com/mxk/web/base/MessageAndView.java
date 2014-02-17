package com.mxk.web.base;

import com.mxk.web.http.ServiceResponse;


/**
 * 数据的封装
 * @author Administrator
 *
 */
public class MessageAndView {

	public int state;
	public String message;
	public Object data;
	private MessageAndViewMediaType type;
	
	private MessageAndView(int state,String message,MessageAndViewMediaType type){
		this.state = state;
		this.message = message;
		this.type = type;
	}
	
	public MessageAndView put(Object data){
		this.data = data;
		return this;
	}
	
	/** 创建一个实例 */
	public static MessageAndView newInstance() {
		return new MessageAndView(ServiceResponse.SUCCESS.getCode(),ServiceResponse.SUCCESS.getDesc(),MessageAndViewMediaType.JSON);
	}
	
	public static MessageAndView newInstance(MessageAndViewMediaType type) {
		return new MessageAndView(ServiceResponse.SUCCESS.getCode(),ServiceResponse.SUCCESS.getDesc(),type);
	}
	
	public static MessageAndView newInstance(ServiceResponse serviceResponse ,String message,MessageAndViewMediaType type) {
		return new MessageAndView(serviceResponse.getCode(),message,type);
	}
	
	public static MessageAndView newInstance(ServiceResponse serviceResponse ,String message) {
		return new MessageAndView(serviceResponse.getCode(),message,MessageAndViewMediaType.JSON);
	}
	
	public enum MessageAndViewMediaType
	{
		TEXT("text/html; charset=UTF-8"),
		JSON("application/json; charset=UTF-8");
		
		private String type;
		
		private MessageAndViewMediaType(String type){
			this.type = type;
		}

		public String getType() {
			return type;
		}
		
	}

	public MessageAndViewMediaType getType() {
		return type;
	}
	
}

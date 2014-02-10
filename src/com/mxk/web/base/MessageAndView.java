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
	
	private MessageAndView(int state,String message){
		this.state = state;
		this.message = message;
	}
	
	public MessageAndView put(Object data){
		this.data = data;
		return this;
	}
	
	/** 创建一个实例 */
	public static MessageAndView newInstance() {
		return new MessageAndView(ServiceResponse.SUCCESS.getCode(),ServiceResponse.SUCCESS.getDesc());
	}
	
	public static MessageAndView newInstance(ServiceResponse serviceResponse ,String message) {
		return new MessageAndView(serviceResponse.getCode(),message);
	}
	
}

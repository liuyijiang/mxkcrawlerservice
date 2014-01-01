package com.mxk.crawler.model;
/**
 * 资源类型枚举
 * @author liuyijiang
 *
 */
public enum ResourceType {

    SHIP("ship"),TANK("tank"),AIRCRAFT("aircraft"),SOLDIER("soldier"),SCENE("scene");
	
	private String code;
	
	private ResourceType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}

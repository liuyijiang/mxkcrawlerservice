package com.mxk.system.catalog;
/**
 * 编目资源类型
 * @author Administrator
 *
 */
public enum CatalogResourceType {
   
	SHIP(1,"战舰模型"),
	TANK(2,"装甲模型"),
	AIRCRAFT(3,"战机模型"),
	SOLDIER(4,"兵人模型"),
	SCENE(5,"场景模型");
	
	private int code;
	
	private String show;
	
	private CatalogResourceType(int code,String show){
		this.code = code;
		this.show = show;
	}

	public int getCode() {
		return code;
	}

	public String getShow() {
		return show;
	}
	
	
	
}

package com.mxk.crawler.model;
/**
 * 资源状态枚举
 * @author Administrator
 *
 */
public enum ResourceState {
	
	/** 链接没有图片*/
	LINK_NO_IMAGE(9),
	
	/** 链接没有资源*/
	LINK_NO_RESOURCE(6),
	
	/** 链接没有被爬取 */
	NO_CRAWLER(1),
	
	/** 正在爬取 */
	CARWLERING(2),
	
	/** 已经爬取 */
	CRAWLERD(3),
	
	/** 没有编目 */
	NO_CATALOGO(4),
	
	/** 已经编目 */
	CATALOGOED(5),
	
	/** 未上传到资源库 */
	NO_UPLOAD(7),
	
	/** 已上传到资源库  */
	UPLOADED(8),
	
	/** 上传资源库失败  */
	UPLOADE_FAIL(10),
	
	/** 链接读取超时  */
	LINK_READ_TIMED_OUT(11);
	
	private int code;
	
	private ResourceState(int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	
	
}

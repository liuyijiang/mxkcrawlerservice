package com.mxk.web.base;


/**
 * 支持扩展
 * @author Administrator
 *
 */
public interface PlusAble {

	public <T extends PlusAble> T copy(Object object);
	
}

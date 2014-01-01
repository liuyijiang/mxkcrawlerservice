package com.mxk.crawler.model;

import java.util.Comparator;


/**
 * 自定义排序实现
 */
@SuppressWarnings("rawtypes")
public class SortableComparator implements Comparator {

	private boolean desc;
	
	public SortableComparator(){
		this(false);
	}
	
	public SortableComparator(boolean desc){
		this.desc = desc;
	}
	
	@Override
	public int compare(Object o1, Object o2) {
		Sortable sort0 = (Sortable)o1;
		Sortable sort1 = (Sortable)o2; 
		int flag = sort0.getSort().compareTo(sort1.getSort());
		if(desc){
		   flag = 0 - flag;	
		}
		return flag;
	}

}

package com.mxk.text;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mxk.web.index.IndexService;

public class TestIndexService {
  
  
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IndexService indexService = context.getBean(IndexService.class);
		long startTime=System.currentTimeMillis(); 
		System.out.println(indexService);
		indexService.createIndex();
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("程序运行时间： "+((endTime - startTime) / 1000 )+"s");  
	}
	
	@Test 
	public void testJunit(){
		System.out.println(1);
	}
	
}

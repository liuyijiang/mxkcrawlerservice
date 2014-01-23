package com.mxk.text;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mxk.web.index.IndexService;

public class TestIndexService {
  
  
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IndexService indexService = context.getBean(IndexService.class);
		System.out.println(indexService);
	}
	
	@Test 
	public void testJunit(){
		System.out.println(1);
	}
	
}

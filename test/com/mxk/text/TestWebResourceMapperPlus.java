package com.mxk.text;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mxk.model.WebResource;
import com.mxk.web.resource.WebResourceMapperPlus;

public class TestWebResourceMapperPlus {
  
	private int sum=0;
	
	@Test
	public void testWebMapPlus(){
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		WebResourceMapperPlus plus = context.getBean(WebResourceMapperPlus.class);
		//List<WebResource> list = plus.selectlimit(0);
		loadData(0,plus);
		//plus.
	}
	
	private void loadData(int id,WebResourceMapperPlus plus){
		List<WebResource> list = plus.selectlimit(id);
		if(list.size() != 0){
			int lastid=0;
			for (WebResource web:list) {
				//System.out.println(web.getTitle());
				lastid = web.getId();
				sum++;
			}
			loadData(lastid,plus);
		}else{
			System.out.println("ok"+sum);
		}
	}
}

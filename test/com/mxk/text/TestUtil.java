package com.mxk.text;

import org.junit.Test;

import com.mxk.util.HttpUtil;

public class TestUtil {
  
	@Test
	public void testHttpUtil(){
		String url = "http://www.xiaot.com/data/attachment/forum/month_1208/120810123481dadc110d55af3f.jpg";
		HttpUtil h = new HttpUtil();
		h.getImageByte(url);
	   
	}
	
}

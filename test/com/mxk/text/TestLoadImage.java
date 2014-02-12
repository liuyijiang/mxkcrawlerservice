package com.mxk.text;

import org.junit.Test;

import com.mxk.util.HttpUtil;

public class TestLoadImage {

	@Test
	public void testLoadImage(){
		//http://www.imx365.com/images/rand_176/album/image/aaronwyo/20041105004536/6168p5.jpg
		//"http://www.waileecn.com/mxk/image/52b815210cf24a645fafe72d_mini.png
		//http://www.worldwar2cn.com/photo/WinterPAKgun.JPG
		//byte[] byteFile = HttpUtil.getImageByte("http://www.worldwar2cn.com/photo/WinterPAKgun.JPG");
		String str = "FKI 316,France,1944。龙的套件，VM PE，FM履带。                      绝对精华~~~！！加入网站作品";
		str = str.replaceAll(" ", "");
		System.out.println(str);
		//byte[] byteFile = HttpUtil.getImageByte("http://www.militaryhobbies.ca/custom/productzoom/dml6233.jpg");
		//System.out.println(byteFile.length);
	}
	
}

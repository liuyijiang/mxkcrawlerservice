package com.mxk.text;

import java.io.File;

import org.junit.Test;

import com.mxk.util.StringUtil;

public class TestImageDownload {
  
	@Test
	public void testDownLoadImage(){
//		String str = StringUtil.cutOutUrlFileName("http://www.militarymodelling.com/sites/1/images/member_albums/48461/fp1 [desktop resolution].jpg");
//		System.out.println(str);
		File file = new File("D:\\mxk-test\\images\\20140214\\103171-10404.jpg");
		System.out.println("ok");
//		Content content = new Content();
//		byte[] byteFile = HttpUtil.getImageByte(content.getSimpleImage());
//		if(byteFile != null){
//			String fileName = StringUtil.cutOutUrlFileName(content.getSimpleImage());
//			String foldler = StringUtil.dateToString(new Date(), "yyyyMMdd");
//			String simpleImage = baseFileUploadService.saveFile(byteFile, fileName , foldler);
//			resource.setSimpleImage(simpleImage);//图片保存成功后
//			if(simpleImage != null){
//				resource.setSimpleImageName( foldler + "/" + fileName);
//				StringBuilder sb = new StringBuilder();
//				for(String img : content.getImages()){
//					sb.append(img+",");
//				}
//				resource.setImages(sb.toString());
//			}
	}
	
}

package com.mxk.web.resource;

import java.io.File;

/**
 * 
 * @author Administrator
 *
 */
public class WebResourceService {

	public static void main(String[] args) {
		File file = new File("D:\\images");
		File filelist[] = file.listFiles();
		for (File files : filelist ) {
			System.out.println(files.getName());
		}
	}
	
}

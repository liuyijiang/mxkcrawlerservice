package com.mxk.web.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mxk.crawler.model.ContentResource;
/**
 * 互联网资源数据接口
 * @author Administrator
 *
 */
@Controller
public class WebResourceController {
   
	@Autowired
	private WebResourceService webResourceService;
	
	/**
	 * 接收爬取出来的数据
	 * @param contentResource
	 * @return
	 */
	@RequestMapping(value = "/add/webresources", method = {RequestMethod.POST})
	@ResponseBody
	public int uploadData(ContentResource contentResource){
		return webResourceService.saveWebResource(contentResource);
	}
	
}

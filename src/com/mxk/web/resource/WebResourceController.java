package com.mxk.web.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mxk.crawler.model.ContentResource;
import com.mxk.model.WebResource;
import com.mxk.web.base.MessageAndView;
import com.mxk.web.security.SecurityDescription;
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
	@SecurityDescription
	public int uploadData(ContentResource contentResource){
		return webResourceService.saveWebResource(contentResource);
	}
	
	/**
	 * 查询单个数据 consumes="application/json", produces = "application/json"
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/resource", method = {RequestMethod.GET} )
	@ResponseBody
	@SecurityDescription
	public MessageAndView findWebResourceById(@RequestParam("id") int id){
		WebResource webResource = webResourceService.selectByWebResourceId(id);
		return MessageAndView.newInstance().put(webResource);
	}
	
	@RequestMapping(value = "/webresources/significance", method = {RequestMethod.POST,RequestMethod.GET} )
	@ResponseBody
	@SecurityDescription
	public MessageAndView updateWebResourceSignificance(@RequestParam("id") int id,@RequestParam("significance") boolean significance){
		webResourceService.updateWebResourceSignificance(id, significance);
		return MessageAndView.newInstance();
	}
}

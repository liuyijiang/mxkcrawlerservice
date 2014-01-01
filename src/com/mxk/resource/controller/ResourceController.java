package com.mxk.resource.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mxk.crawler.model.ContentResource;
import com.mxk.crawler.model.ResourceType;
import com.mxk.crawler.model.Tag;
import com.mxk.crawler.model.WebResource;
import com.mxk.resource.service.ResourceService;
import com.mxk.resource.web.ResourceVO;
import com.mxk.system.SystemService;
import com.mxk.util.StringUtil;
/**
 * 
 * @author Administrator
 *
 */
@Controller
public class ResourceController {
 
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private SystemService systemService;
	
	//@RequestParam("tags") String tgas)
	/**
	 * @RequestParam 不能超过4个
	 * @param type
	 * @param name
	 * @param keyword
	 * @param tgas
	 * @return
	 */
	//@PathVariable("type") String type, @RequestParam("name") String name,@RequestParam("keyword") String keyword,@RequestParam("tags") String tgas,HttpSession session
	@RequestMapping(value = "/add/resources", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView addResource(ResourceVO vo){
		WebResource re = new WebResource();
		re.setKeyword(vo.getKeyword());
		re.setName(vo.getName());
		re.setQuantity(0);
		re.setType(vo.getType());
		re.setLastUpdateTime(StringUtil.dateToString(new Date()));
		re.setRead((int)(Math.random()*100));
		re.setCollect((int)(Math.random()*10));
		String[] tags = vo.getTags().split(",");
		re.setType(ResourceType.SHIP.toString());
		Set<String> set = new HashSet<String>();
		Collections.addAll(set, tags);  
		re.setTag(set);
		resourceService.saveResource(re);
		ModelAndView mv = new ModelAndView("redirect:/type/"+ResourceType.SHIP.getCode()+"/resource");
		return mv;
	}
	
	/**
	 * 添加资源界面
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/type/{type}/resource", method = RequestMethod.GET)
	public ModelAndView allResource(@PathVariable("type") String type){
		List<Tag> list = systemService.findTagsByType(ResourceType.valueOf(type.toUpperCase()));
		ModelAndView mv = new ModelAndView("resource.jsp");
		mv.getModelMap().put("tags", list);
		mv.getModelMap().put("type", type);
		return mv;
	}
	
	/**
	 * 模糊查询
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/find/key/resource", method = RequestMethod.POST)
	@ResponseBody
	public List<WebResource> findWebResourceByKeyWord(@RequestParam("keyword") String keyword){
		return resourceService.findResourceByKeyWord(keyword);
	}
	
	/**
	 * 编目帖子信息
	 * @return
	 */
	@RequestMapping(value = "/contexts", method = RequestMethod.GET)
	public ModelAndView showContext(){
		List<String> type = new ArrayList<String>();
		for(ResourceType r :ResourceType.values()){
			type.add(r.getCode());
		}
		List<ContentResource> list = resourceService.findContextResourceNotCatalog();
		ModelAndView mv = new ModelAndView("context.jsp");
		mv.getModelMap().put("type", type);
		mv.getModelMap().put("list", list);
		return mv;
	}
	
	@RequestMapping(value = "/regex/contexts", method = RequestMethod.POST)
	public ModelAndView showContextRegex(@RequestParam("keyword") String keyword){
		List<String> type = new ArrayList<String>();
		for(ResourceType r :ResourceType.values()){
			type.add(r.getCode());
		}
		List<ContentResource> list = resourceService.findContextResourceNotCatalogRegex(keyword);
		ModelAndView mv = new ModelAndView("context.jsp");
		mv.getModelMap().put("type", type);
		mv.getModelMap().put("list", list);
		return mv;
	}
}

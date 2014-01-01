package com.mxk.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mxk.crawler.CrawlerService;
import com.mxk.crawler.CrawlerTask;
import com.mxk.crawler.base.CrawlerStateInfo;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;
import com.mxk.crawler.model.ResourceType;
import com.mxk.crawler.model.SubTag;
import com.mxk.crawler.model.Tag;

/**
 * 爬取资源服务器
 * @author Administrator
 *
 */
@Controller
public class SystemController {
  
	public static final Logger logger = LoggerFactory.getLogger(SystemController.class);
	
	@Autowired
	private CrawlerService crawlerService;
	
	@Autowired
	private CrawlerTask crawlertask;
	
	@Autowired
	private SystemService systemService;
	
	/**
	 * 系统主页
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(){
		List<String> type = new ArrayList<String>();
		for(ResourceType r :ResourceType.values()){
			type.add(r.getCode());
		}
		ModelAndView mv = new ModelAndView("index.jsp");
		mv.getModelMap().put("type", type);
		return mv;
	}
	
	/**
	 * 爬取器状态
	 * @return
	 */
	@RequestMapping(value = "/crawlers", method = RequestMethod.GET)
	public ModelAndView crawlers(){
		List<CrawlerStateInfo> list = crawlertask.getCrawlerStateInfo();
		ModelAndView mv = new ModelAndView("crawler.jsp");
		mv.getModelMap().put("list", list);
		return mv;
	}
	
	/**
	 * 启动或停止爬取
	 * @param crawler
	 * @return
	 */
	@RequestMapping(value = "/crawle", method = RequestMethod.POST)
	public ModelAndView startOrStopCrawler(@RequestParam("crawlerName") String crawlerName,@RequestParam("runable") boolean runable){
		ModelAndView mv = new ModelAndView("index.jsp");
		mv.getModelMap().put("message", "操作完成");
		crawlertask.notifyAllCrawlerTask(crawlerName,runable);
		return mv;
	}
	
	
	/**
	 * 添加爬取网站的初始页面
	 * @param links
	 * @param match
	 * @return
	 */
	@RequestMapping(value = "/add/links", method = RequestMethod.POST)
	public ModelAndView addLinks(@RequestParam("links") String links,@RequestParam("match") String match){
		ModelAndView mv = new ModelAndView("index.jsp");
		List<Links> list = new ArrayList<Links>();
		if(StringUtils.isNotEmpty(links)){
			String[] linksarry = links.split(",");
			for (String str : linksarry){
				Links link = new Links();
				link.setCreateTime(new Date());
				link.setUrl(str);
				link.setMatchUrl(match);
				link.setState(ResourceState.NO_CRAWLER.getCode());
				list.add(link);
			}
			crawlerService.saveLink(list);
		}
		mv.getModelMap().put("message", "操作完成");
		return mv;
	}
	
	
	/**
	 * 标签页面
	 * @return
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public ModelAndView tags(){
		List<String> type = new ArrayList<String>();
		for(ResourceType r :ResourceType.values()){
			type.add(r.getCode());
		}
		ModelAndView mv = new ModelAndView("tag.jsp");
		mv.getModelMap().put("type", type);
		return mv;
	}
	
	/**
	 * 新建标签
	 * @param name
	 * @param type
	 * @param sort
	 * @return
	 */
	@RequestMapping(value = "/add/tag", method = RequestMethod.POST)
	public ModelAndView addtag(@RequestParam("name") String name,@RequestParam("type") String type,@RequestParam("sort") int sort,@RequestParam("subtags") String subtags){
		ModelAndView mv = new ModelAndView("redirect:/tags"); //"forward:/hello";
		Tag tag = new Tag();
		tag.setType(type);
		tag.setName(name);
		tag.setSort(sort);
		tag.setCreateTime(new Date());
		String[] subtag = subtags.split(","); //中国#1，日本#2
		List<SubTag> list = new ArrayList<SubTag>();
		tag.setSubtags(list);
		for(String str : subtag){
			String[] sb = str.split("#");
			SubTag st = new SubTag();
			st.setDesc(sb[0]);
			st.setSort(sb[1]);
			list.add(st);
		}
		systemService.saveTag(tag);
		return mv;
	}
	
	/**
	 * 根据类型查询标签
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/{type}/tags/", method = RequestMethod.GET)
	@ResponseBody
	public List<Tag> findTagsByType(@PathVariable("type") String type){
		List<Tag> list = new ArrayList<Tag>();
		list = systemService.findTagsByType(ResourceType.valueOf(type.toUpperCase()));
		return list;
	}
	
	//public String add
	
}

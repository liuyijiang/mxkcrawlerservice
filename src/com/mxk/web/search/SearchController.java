package com.mxk.web.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mxk.web.index.IndexService;
import com.mxk.web.security.SecurityDescription;

/**
 * 查询的web界面
 * @author liuyijiang
 *
 */
@Controller
public class SearchController {

    public static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
    @Autowired
    private IndexService indexService;
    
    @Autowired
    private SearchLogService searchLogService;
	/**
	 * 文章主页
	 * @return
	 */
//	@RequestMapping(value = "/index", method = RequestMethod.GET)
//	public ModelAndView index(){
//		return new ModelAndView("/search/index.jsp");
//	}
	
	@RequestMapping(value = "/search", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SecurityDescription
	public PageModel search(@RequestParam("keyword") String keyword, @RequestParam("currentPage") int currentPage){
		//TODO 线程池
		searchLogService.saveUserSearchInfo(keyword);//保存用户查询的内容
		return indexService.searchIndex(keyword, "content" ,currentPage);
	}
	
}

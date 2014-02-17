package com.mxk.web.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mxk.web.base.MessageAndView;
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
    private SearchService searchService;
	/**
	 * 文章主页
	 * @return
	 */
//	@RequestMapping(value = "/index", method = RequestMethod.GET)
//	public ModelAndView index(){
//		return new ModelAndView("/search/index.jsp");
//	}
	
    /**
     * 搜索资源
     * @param keyword
     * @param currentPage
     * @return
     */
	@RequestMapping(value = "/search", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SecurityDescription
	public MessageAndView search(@RequestParam("keyword") String keyword, @RequestParam("currentPage") int currentPage){
		PageModel model = indexService.searchIndex(keyword, "content" ,currentPage);
		//searchService.saveUserSearchInfo(keyword);//保存用户查询的内容
		return MessageAndView.newInstance().put(model);
	}
	
	/**
	 * 试试手气
	 * @return
	 */
	@RequestMapping(value = "/luck/search", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SecurityDescription
	public MessageAndView luckSearch(){
		PageModel model = searchService.luckSearch();
		return MessageAndView.newInstance().put(model);
	}
	
}

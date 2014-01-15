package com.mxk.web.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mxk.web.index.IndexService;

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
    
	/**
	 * 文章主页
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(){
		return new ModelAndView("/search/index.jsp");
	}
	
	@RequestMapping(value = "/search", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public List<SearchRespone> search(@RequestParam("keyword") String keyword){
		return indexService.searchIndex(keyword, "title");
	}
	
}

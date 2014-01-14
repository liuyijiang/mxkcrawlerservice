package com.mxk.web.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 查询的web界面
 * @author liuyijiang
 *
 */
@Controller
public class SearchController {

    public static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	/**
	 * 文章主页
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(){
		return new ModelAndView("/search/index.jsp");
	}
	
	
}

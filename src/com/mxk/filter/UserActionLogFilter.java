package com.mxk.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mxk.crawler.model.UserActionLog;
import com.mxk.system.SystemService;

/**
 * 记录用户的每一个操作 数据统计用
 * @author Administrator
 *
 */
public class UserActionLogFilter implements Filter {
	
	public static final Logger logger = LoggerFactory.getLogger(UserActionLogFilter.class);
	
	@Autowired
	private SystemService systemService;
	
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		//HttpServletResponse response = (HttpServletResponse) arg1;
		String requsturl = request.getRequestURL().toString();
		if(requsturl.contains(".html") || requsturl.contains(".do")){
			long start = System.currentTimeMillis();
			UserActionLog log = new UserActionLog();
			log.setClient(checkRequestFrom(request.getHeader("User-Agent").toLowerCase()));
			log.setCreateTime(new Date());
			log.setParmas(getRequestParam(request.getParameterMap()));
			log.setUrl(requsturl);
			log.setRequest(requsturl.substring(requsturl.lastIndexOf("/"),requsturl.length()));
			log.setIp(request.getRemoteAddr());
			log.setMethod(request.getMethod());
			log.setSuccess("success");
			try{
			   filter.doFilter(arg0, arg1);
			}catch(Exception e){
				log.setSuccess("fail");	
			}
			long end = System.currentTimeMillis();
			log.setActionTime((int)(end - start));
			systemService.saveUserActionLog(log);
		}else{
			filter.doFilter(arg0, arg1);
		}
		
	}

	private String checkRequestFrom(String userAgent){
		if(userAgent.contains(ActionConstant.APPLEWEBKIT.getShow()) || userAgent.contains(ActionConstant.MOBILE.getShow()) || userAgent.contains(ActionConstant.ANDROID.getShow())){
			return ActionConstant.PHONEBROWSER.getShow();
		}else if(userAgent.contains(ActionConstant.APP.getShow())){
			return ActionConstant.APP.getShow();
		}else{
			return ActionConstant.BROWSER.getShow();
		}
	}
	
	private String getRequestParam(Map<String,String[]> map){
		StringBuilder sb = new StringBuilder();
		Set<Entry<String,String[]>> set = map.entrySet();
		sb.append("[");
		for(Entry<String,String[]> entry : set){
			sb.append(entry.getKey()+"=");
			for (String str : entry.getValue() ) {
				sb.append(str);
			}
			sb.append("|");
		}
		sb.append("]");
		return sb.toString();
	}
	
	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("启动用户操作日志记录过滤器");
	}

	@Override
	public void destroy() {
		logger.info("销毁用户操作日志记录过滤器");
	}
	
	/**
	 * 内部的枚举对象
	 * @author Administrator
	 *
	 */
	public enum ActionConstant{
		
		APPLEWEBKIT(1,"applewebkit"),
		MOBILE(2,"mobile"),
		ANDROID(3,"android"),
		
		/** */
		BROWSER(4,"browser"),
		/** */
		PHONEBROWSER(5,"phonebrowser"),
		/** */
		APP(6,"mxkapp");
		
		private int code;
		private String show;
		
		private ActionConstant(int code,String show){
			this.code = code;
			this.show = show;
		}
		public int getCode() {
			return code;
		}
		public String getShow() {
			return show;
		}
		
	}
	
}

package com.mxk.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取当前线程的请求的ip 用户id 请求的url
 * @author Administrator
 *
 */
public class CourntThreadUserMessageFilter implements Filter {
	
	public static final Logger logger = LoggerFactory.getLogger(CourntThreadUserMessageFilter.class);
	
	private static ThreadLocal<CourntThreadUserMessage> THREAD_OBJECT = new ThreadLocal<CourntThreadUserMessage>();
	
	/**
	 * 当前线程用户信息
	 * @author Administrator
	 *
	 */
	public static class CourntThreadUserMessage {
		HttpServletRequest request;
		HttpServletResponse response;
	    public int userid;
	    public String ip;
	    public String requestUrl;
	}
	
	
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		String rqeustUrl = request.getRequestURL().toString();
		Cookie[] cookies = request.getCookies();
		CourntThreadUserMessage value = new CourntThreadUserMessage();
		THREAD_OBJECT.set(value);
		THREAD_OBJECT.get().ip = request.getRemoteAddr();
		//request.get
		
		THREAD_OBJECT.get().request = request;
		THREAD_OBJECT.get().response = response;
		THREAD_OBJECT.get().userid = 0;
		THREAD_OBJECT.get().requestUrl = rqeustUrl;
		//THREAD_OBJECT.get()
//		for(){
//			
//		}
		
		
		System.out.println(request.getRemoteAddr());
//			if(rqeusturl.contains("system")){ //如果是
//				String c = request.getContextPath();
//				int p = request.getServerPort();
//				response.sendRedirect(request.getContextPath()+"/nopermission.html");
//			}
		filter.doFilter(arg0, arg1);
	}
	
	public static CourntThreadUserMessage getCourntThreadUserMessage(){
		return THREAD_OBJECT.get();
	}
	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("用户请求信息过滤器启动");
	}

	@Override
	public void destroy() {
		
	}
	
}

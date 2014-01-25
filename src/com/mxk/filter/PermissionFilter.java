package com.mxk.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.mxk.web.security.SecurityInterceptorAdapter;

/**
 * 过滤可以打开的页面
 * @author Administrator
 *
 */
public class PermissionFilter implements Filter {
	
	public static final Logger logger = LoggerFactory.getLogger(PermissionFilter.class);
	
	//@Value("${mxk.permission.difilter}")
	private boolean dofilter = false; //是否要过滤
	

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		if(dofilter){
			String rqeusturl = request.getRequestURL().toString();
			if(rqeusturl.contains("system")){ //如果是
//				String c = request.getContextPath();
//				int p = request.getServerPort();
				response.sendRedirect(request.getContextPath()+"/nopermission.html");
			}
		}
		filter.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("启动访问权限过滤器");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}

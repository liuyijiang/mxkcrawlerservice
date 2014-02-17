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

import com.mxk.mail.MailUtil;
import com.mxk.util.SecurityUtil;

/**
 * 过滤可以打开的页面
 * @author Administrator
 *
 */
public class PermissionFilter implements Filter {
	
	public static final Logger logger = LoggerFactory.getLogger(PermissionFilter.class);
	
	private static final String ENCRYPT_KEY = "etoken";
	
	private boolean dofilter = true; //是否要过滤
	
    private static final String KEY = "Xting87LiuyiJ89";
	
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		try{
			if(dofilter){
				String rqeusturl = request.getRequestURL().toString();
				if(rqeusturl.contains("system")){ //如果是
					if(!rqeusturl.contains("slogin")){
						if(!accreditVerify(request,response)){
							String ip = request.getRemoteAddr();
							//MailUtil.simpleMail(new String[]{"liuyijiang3430@qq.com"},"危险警告-未授权请求","危险警告-未授权请求  ip地址："+ip+" 请求路径："+rqeusturl);
							response.sendRedirect(request.getContextPath()+"/nopermission.html");
						}
					}
				}
			}
		}catch(Exception e){
		    logger.error("过滤异常{}",e);	
		}
		filter.doFilter(arg0, arg1);
	}
	
	/**
	 * 授权验证 用于远程接口调用
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean	accreditVerify(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Cookie[] cookies = request.getCookies();
		boolean accredit = false;
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(ENCRYPT_KEY.equals(cookie.getName())){
					String token = cookie.getValue();
					if(KEY.equals(token)){
						accredit = true;
						break;
					}
				}
			}
		}
		return accredit;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("启动访问权限过滤器");
	}

	@Override
	public void destroy() {
		logger.info("销毁访问权限过滤器");
	}
	
}

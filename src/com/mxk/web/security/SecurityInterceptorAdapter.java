package com.mxk.web.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mxk.util.SecurityUtil;

/**
 * 接口权限拦截器
 * @author Administrator
 *
 */
public class SecurityInterceptorAdapter extends HandlerInterceptorAdapter  {

	private static final String ENCRYPT_KEY = "token";
	
	public static final Logger logger = LoggerFactory.getLogger(SecurityInterceptorAdapter.class);
	
	@Value("${mxk.security.password}")
	private String password;
	
	@Value("${mxk.security.token}")
	private String token;
	
	/**
	 * 授权验证
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
					String str = cookie.getValue();
					System.out.println(str);
					byte[] tokenByte = SecurityUtil.string64ToString(str+"=="); //可以考虑使用ecode cookie中
					String decryptToken = SecurityUtil.decrypt(tokenByte, password);
					if(token.equals(decryptToken)){
						accredit = true;
						break;
					}
				}
			}
		}
		return accredit;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Exception {
		if (obj instanceof HandlerMethod) {
			
			HandlerMethod handler = (HandlerMethod) obj;
			SecurityDescription securityDescription = handler.getMethodAnnotation(SecurityDescription.class);
		    if(securityDescription != null){
				if(securityDescription.accredit()){ //需要授权验证
			    	if(!accreditVerify(request,response)){
				    	logger.info("未授权的请求 请求链接{} 请求ip地址{}",request.getRequestURL().toString(), request.getRemoteAddr());
				    	return false;
			    	}
			    }
			    if(securityDescription.loginRequest()){
			    	
			    }
		    }
		}  
		return super.preHandle(request, response, obj);
	}
		
}

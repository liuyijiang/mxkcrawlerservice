package com.mxk.web.user;

import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mxk.cache.CacheService;
import com.mxk.mail.MailService;
import com.mxk.util.SecurityUtil;
import com.mxk.util.StringUtil;
import com.mxk.util.ValidateUtil;
import com.mxk.web.base.MessageAndView;
import com.mxk.web.http.ServiceResponse;
import com.mxk.web.security.SecurityDescription;

/**
 * 用户接口Controller
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/user")
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Value("${file.image.user.default.mini}")
	private String defaultImage;
	
	/** 用户service*/
	@Resource
	private UserService userService;
	
	/**邮件服务 */
	@Resource
	private MailService mailService;
	
	/**缓存服务 */
	@Resource
	private CacheService cacheService;
	
	@RequestMapping(value = "/regist",method = {RequestMethod.POST})
	@ResponseBody
	@SecurityDescription(loginRequest=false)
	public MessageAndView userRegist(@RequestParam("email") String email,
			@RequestParam("username") String username,
			@RequestParam("password") String password) {
		UserPlus user = createUserPlus(username,password,email);
		if (!valiateUserPlus(user)){ //安全验证
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"数据输入有误");
		}
		if (!userService.uniqueUserEmailValiate(user.getUserEmail())) { //唯一性验证
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"注册邮箱已被占用");
		}
		if (!userService.uniqueUserEmailValiate(user.getUserName())) { //唯一性验证
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"用户名已被占用");
		}
		//TODO 缓存
		//userService.saveUser(user);
		//mailService.sendSimpleEmail(user.getUserEmail(), "新用户注册成功", "欢迎你：" + user.getUserName() +" 注册模型控！");
		//mailService.sendSimpleEmail(mailService.getAdminHost(), "新用户注册", "来自web的用户："+JSON.toJSONString(user));
		//创建一个uuidtoken 生成一个uuid 在redis key-value 获得  有登录了的 没有就是没有登录
		user.setToken(SecurityUtil.getBASE64(UUID.randomUUID().toString()));
		//cacheService.set(user.getToken(), user.getId());
		cacheService.set(user.getToken(), 1);
		return MessageAndView.newInstance().put(user);
	}
	
	/**
	 * 验证传入信息
	 * @param userRegisterRequest
	 * @return
	 */
	private boolean valiateUserPlus(UserPlus plus){
		if ( StringUtil.stringIsEmpty(plus.getUserName()) ) {
			return false;
		}
		if ( StringUtil.stringIsEmpty(plus.getUserPassword()) ) {
			return false;
		}
		if ( !ValidateUtil.isEmail(plus.getUserEmail())) {
			return false;
		}
		return true;
	}
	
	/**
	 * 创建UserPlus
	 * @return
	 */
	private UserPlus createUserPlus(String name,String password,String email){
		//TODO xss  sql 注入过滤 有xss sql 需要邮件报警
		UserPlus plus = new UserPlus();
		plus.setUserEmail(email);
		plus.setUserName(name);
		//plus.setUserImage(defaultImage);
		plus.setUserImage("http://www.waileecn.com/mxk/image/userheaderimin.png");
		plus.setUserPassword(SecurityUtil.digestByMd5(password));
		return plus;
	}

}

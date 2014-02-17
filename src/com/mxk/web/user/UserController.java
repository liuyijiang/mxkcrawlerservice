package com.mxk.web.user;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mxk.model.User;
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
	
	/**
	 * 用户注册
	 * @param email
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/regist",method = {RequestMethod.POST})
	@ResponseBody
	@SecurityDescription(loginRequest = false)
	public MessageAndView userRegist(@RequestParam("email") String email,
			@RequestParam("username") String username,
			@RequestParam("password") String password) {
		User user = createUserPlus(username,password,email);
		if (!valiateUserPlus(user)){ //安全验证
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"数据输入有误");
		}
		if (!userService.uniqueUserEmailValiate(user.getUserEmail())) { //唯一性验证
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"注册邮箱已被占用");
		}
		if (!userService.uniqueUserNameValiate(user.getUserName())) { //唯一性验证
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"用户名已被占用");
		}
		//TODO 统一的异常处理filter类
		UserPlus userplus = userService.userRegist(user);
		return MessageAndView.newInstance().put(userplus);
	}
	
	/**
	 * 用户登陆
	 * @param email
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login",method = {RequestMethod.POST})
	@ResponseBody
	@SecurityDescription(loginRequest = false)
	public MessageAndView userlogin(@RequestParam("email") String email,
			@RequestParam("password") String password) {
		if(!valiateLoginParm(email,password)){
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"数据输入有误");
		}
		UserPlus user = userService.userLogin(email, password);
		if(user == null){
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"没有这个用户");
		}
		return MessageAndView.newInstance().put(user);
	}
	
	/**
	 * 用户退出
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/loginout",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	@SecurityDescription(loginRequest = true)
	public MessageAndView userLoginOut(@RequestParam("id") int id, @CookieValue("token") String token){
		if(token != null){
			userService.userLoginOut(token, id);
		}
		return MessageAndView.newInstance(ServiceResponse.SUCCESS,"操作成功");	
	}
	
	
	/**
	 * 主键查询用户
	 * @param id
	 * @return
	 */ 
	@RequestMapping(value = "/{id}",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	@SecurityDescription(loginRequest = false)
	public MessageAndView userInfo(@PathVariable("id") int id){
		UserPlus user = userService.findUserById(id);
		return MessageAndView.newInstance().put(user);
	}
	
	/**
	 * 保存用户收藏信息
	 * @param userCollectPlus
	 * @return
	 */
	@RequestMapping(value = "/collect",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	@SecurityDescription(loginRequest = false)
	public MessageAndView userCollect(UserCollectPlus userCollectPlus){
		if(!validateCollect(userCollectPlus)){
			return MessageAndView.newInstance(ServiceResponse.SERVICE_ERROR,"数据输入有误");
		}
		userService.userCollect(userCollectPlus);
		return MessageAndView.newInstance();
	}
	
	
	private boolean validateCollect(UserCollectPlus userCollectPlus){
		if(userCollectPlus.getUserId() == null){
			return false;
		}
		if(userCollectPlus.getColletTarget() == null){
			return false;
		}
		if(userCollectPlus.getColletTargetType() == null){
			return false;
		}
		if(userCollectPlus.getSimpleDesc() == null){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证登陆信息
	 * @param email
	 * @param password
	 * @return
	 */
	private boolean valiateLoginParm(String email,String password){
		if ( StringUtil.stringIsEmpty(password) ) {
			return false;
		}
		if ( !ValidateUtil.isEmail(email)) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 验证传入信息
	 * @param userRegisterRequest
	 * @return
	 */
	private boolean valiateUserPlus(User plus){
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
	private User createUserPlus(String name,String password,String email){
		//TODO xss  sql 注入过滤 有xss sql 需要邮件报警
		User plus = new User();
		plus.setUserEmail(email);
		plus.setUserName(name);
		//plus.setUserImage(defaultImage);
		plus.setUserImage("http://www.waileecn.com/mxk/image/userheaderimin.png");
		plus.setUserPassword(SecurityUtil.digestByMd5(password));
		return plus;
	}
	
}

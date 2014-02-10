package com.mxk.web.user;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mxk.dao.UserMapper;
import com.mxk.model.User;
import com.mxk.model.UserCriteria;

/**
 * 用户接口
 * @author Administrator
 *
 */
@Service
public class UserService {

	public static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Resource
	private UserMapper userMapper;
	
	@Resource
	private UserMapperPlus userMapperPlus;
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public User saveUser(User user){
		try{
			userMapper.insert(user);
			logger.info("用户注册成功");
		}catch(Exception e){
			logger.error("保存用户失败：{} 用户信息：{}",e,JSON.toJSONString(user));
		}
		return user;
	}
	
	/**
	 * 唯一性用户名验证
	 * @param username
	 * @return
	 */
	public boolean uniqueUserNameValiate(String username){
		int num = userMapperPlus.uniqueUserNameValiate(username);
		return num == 0 ? true : false;
	}
	
	/**
	 * 唯一性用户邮件验证
	 * @param useremail
	 * @return
	 */
	public boolean uniqueUserEmailValiate(String useremail){
		int num = userMapperPlus.uniqueUserEmailValiate(useremail);
		return num == 0 ? true : false;
	}
	
}

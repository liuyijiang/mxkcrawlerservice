package com.mxk.web.user;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.mxk.cache.Cacheable;
import com.mxk.cache.CacheableService;
import com.mxk.cache.CacheableType;
import com.mxk.dao.UserMapper;
import com.mxk.exception.MxkException;
import com.mxk.mail.MailService;
import com.mxk.model.User;
import com.mxk.model.UserCriteria;
import com.mxk.util.SecurityUtil;
import com.mxk.util.StringUtil;

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
	
	/**邮件服务 */
	@Resource
	private MailService mailService;
	
	/**缓存服务 */
	@Resource
	private CacheableService cacheService;
	
	/**
	 * 更具用户邮箱和密码查询
	 * @param email
	 * @param password
	 * @return
	 */
	private UserPlus selectUserWithLogin(String email,String password){
		String md5password = SecurityUtil.digestByMd5(password);
		UserCriteria criteria = new UserCriteria();
		criteria.createCriteria().andUserEmailEqualTo(email).andUserPasswordEqualTo(md5password);
		List<User> list = userMapper.selectByExample(criteria);
		if(list.size() > 0){
			return new UserPlus(list.get(0));
		}
		return null;
	}
	
	
	/**
	 * 保存用户
	 * @param user
	 * @return
	 */
	@Cacheable(persist = true, cachetype = CacheableType.CACHE_FOR_INSTER)
	@Transactional
	private UserPlus saveUser(User user){
		try{
			userMapperPlus.insert(user);
			logger.info("用户注册成功");
		}catch(Exception e){
			logger.error("保存用户失败：{} 用户信息：{}",e,JSON.toJSONString(user));
		    throw new MxkException();
		}
		return new UserPlus(user);
	}
	
	public UserPlus userLogin(String email,String password){
		UserPlus user = selectUserWithLogin(email, password);
		if(user != null){
			//创建一个uuidtoken 生成一个uuid 在redis key-value 获得  有登录了的 没有就是没有登录
			user.setToken(SecurityUtil.getBASE64(UUID.randomUUID().toString()));
			cacheService.setExpire(user.getToken(), user.getId());
		}
		return user;
	}
	
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public UserPlus userRegist(User user) {
		UserPlus userPlus = saveUser(user);
		mailService.sendSimpleEmail(user.getUserEmail(), "新用户注册成功", "欢迎你：" + user.getUserName() +" 注册模型控！");
		mailService.sendSimpleEmail(mailService.getAdminHost(), "新用户注册", "来自web的用户："+JSON.toJSONString(user));
		//创建一个uuidtoken 生成一个uuid 在redis key-value 获得  有登录了的 没有就是没有登录
		userPlus.setToken(SecurityUtil.getBASE64(UUID.randomUUID().toString()));
		cacheService.setExpire(userPlus.getToken(), user.getId());
		return userPlus;
	}
	
	/**
	 * 查询用户 主键查询
	 * @param id
	 * @return
	 */
	@Cacheable(persist = false, cachetype = CacheableType.CACHE_FOR_SELECT)
	public UserPlus findUserById(int id){
		User user = null;
		try{
			user = userMapper.selectByPrimaryKey(id);
		}catch(Exception e){
			logger.error("查询用户失败：{} 用户信息：{}",e,id);
		}
		return new UserPlus(user);
	}
	
	/**
	 * 用户退出系统
	 * @param tokn
	 * @param id
	 * @return
	 */
	public boolean userLoginOut(String token,int id){
		boolean success = false;
		if(!StringUtil.stringIsEmpty(token)){
			Integer cachId = cacheService.get(token, Integer.class);
			if(cachId != null && cachId.equals(id)){
				success = cacheService.deleteKey(token);
			}
		}
		return success;
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

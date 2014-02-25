package com.mxk.web.user;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mxk.cache.Cacheable;
import com.mxk.cache.CacheableType;
import com.mxk.dao.UserSubjectMapper;
import com.mxk.dao.UserSubjectPartMapper;
import com.mxk.exception.MxkException;
import com.mxk.model.UserSubject;
import com.mxk.model.UserSubjectCriteria;

/**
 * 用户发布专题信息服务
 * @author Administrator
 *
 */
@Service
public class UserSubjectService {

	public static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Resource
	private UserSubjectMapperPlus userSubjectMapperPlus;
	
	@Resource
	private UserSubjectMapper userSubjectMapper;
	
	@Resource
	private UserSubjectPartMapperPlus userSubjectPartMapperPlus;
	
	@Resource
	private UserSubjectPartMapper userSubjectPartMapper;
	
	@Cacheable(persist = true, cachetype = CacheableType.CACHE_FOR_INSTER)
	public UserSubjectPlus createUserSubject(UserSubjectPlus plus){
		try{
			userSubjectMapperPlus.insert(plus);
			logger.info("新建用户专辑");
		}catch(Exception e){
			logger.error("保存用户专辑失败：{} 专辑信息：{}",e,JSON.toJSONString(plus));
		    throw new MxkException();
		}
		return plus;
	}
	
	@Cacheable(persist = false, cachetype = CacheableType.CACHE_FOR_SELECT)
	public UserSubjectPlus findUserSubjectPlusById(int id){
		UserSubject subject = userSubjectMapper.selectByPrimaryKey(id);
		UserSubjectPlus userSubjectPlus = new UserSubjectPlus(); 
		userSubjectPlus.copy(subject);
		return userSubjectPlus;
	}
	
	@Cacheable(persist = true, cachetype = CacheableType.CACHE_FOR_INSTER)
	public UserSubjectPartPlus createUserPart(UserSubjectPartPlus plus){
		try{
			userSubjectPartMapperPlus.insert(plus);
			logger.info("新建用户专辑part");
		}catch(Exception e){
			logger.error("保存用户专辑part失败：{} 专辑信息：{}",e,JSON.toJSONString(plus));
		    throw new MxkException();
		}
		return plus;
	}
	
	@Cacheable(persist = true, cachetype = CacheableType.CACHE_FOR_UPDATE)
	public UserSubjectPlus updateUserSubject(UserSubjectPlus plus){
		try{
			userSubjectMapper.updateByPrimaryKey(plus);
		}catch(Exception e){
			logger.error("update用户专辑失败：{} 专辑信息：{}",e,JSON.toJSONString(plus));
		    throw new MxkException();
		}
		return plus;
	}
	
	@Cacheable(persist = true, cachetype = CacheableType.CACHE_FOR_UPDATE)
	public UserSubjectPartPlus updateUserSubjectPart(UserSubjectPartPlus plus){
		try{
			userSubjectPartMapper.updateByPrimaryKey(plus);
		}catch(Exception e){
			logger.error("update用户专辑part失败：{} 专辑信息：{}",e,JSON.toJSONString(plus));
		    throw new MxkException();
		}
		return plus;
	}
	
	//加缓存 
	public List<UserSubjectPlus> findUserAllSubject(int id){
		List<UserSubjectPlus> list = new ArrayList<UserSubjectPlus>();
		UserSubjectCriteria criteria = new UserSubjectCriteria();
		criteria.createCriteria().andUseridEqualTo(id);
		for(UserSubject sub : userSubjectMapper.selectByExample(criteria)){
			list.add(new UserSubjectPlus().copy(sub));
		}
		return list;
	}
	
	
	public boolean checkSubjectOwner(int id,int userid){
		logger.debug("id:"+id + "userid:" + userid);
		int count = userSubjectMapperPlus.findSubjectUserId(id,userid);
		return count == 1 ? true : false;
	}
	
}

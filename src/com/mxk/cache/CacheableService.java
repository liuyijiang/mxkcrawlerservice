package com.mxk.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mxk.cache.template.RedisCacheTemplate;
/**
 * 业务类型的缓存服务
 * @author Administrator
 *
 */
@Service
public class CacheableService {

	public static final Logger logger = LoggerFactory.getLogger(CacheableService.class); 
	
	@Autowired
	private RedisCacheTemplate redisCacheTemplate;
	
	public void set(String key,Object value){
		redisCacheTemplate.set(key,value);
	}
	
	public void setExpire(String key,Object value){
		redisCacheTemplate.setExpire(key,value);
	}
	
	public void setExpire(String key,Object value,int time){
		redisCacheTemplate.setExpire(key,value,time);
	}
	
	public boolean deleteKey(String key){
		return redisCacheTemplate.deleteKey(key);
	}

	public <T> T get(Object id,Class<T> clazz) {
		return redisCacheTemplate.get(id, clazz);
	}
	

}

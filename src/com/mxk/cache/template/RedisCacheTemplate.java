package com.mxk.cache.template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.mxk.util.StringUtil;
 
/**
 * redis temple
 * @author liuyijiang
 *
 */
@Component
public class RedisCacheTemplate {

	public static final Logger logger = LoggerFactory.getLogger(RedisCacheTemplate.class); 
	
	@Autowired
	private RedisCachePool jedispool;
	
	@Value("${redis.default.expire.time}")
	private int defaultExpireTime;
	
	/**
	 * 缓存数据
	 * @param id
	 * @param data
	 */
	public void set(Object id,Object data){
		String key = id.toString() + data.getClass().getSimpleName();
		Jedis jedis = null;
		try{
			String value = JSON.toJSONString(data);
			jedis = jedispool.getJedis();
			jedis.set(key, value);
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
		}finally{
			jedispool.returnResource(jedis);
		}
	}
	
	/**
	 * 缓存数据 带过期时间
	 * @param id
	 * @param data
	 */
	public void setExpire(Object id,Object data,int expire){
		String key = id.toString() + data.getClass().getSimpleName();
		Jedis jedis = null;
		try{
			String value = JSON.toJSONString(data);
			jedis = jedispool.getJedis();
			jedis.setex(key, expire, value);
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
		}finally{
			jedispool.returnResource(jedis);
		}	
	}
	
	/**
	 * 缓存数据 带过期时间
	 * @param id
	 * @param data
	 */
	public void setExpire(Object id,Object data){
		setExpire(id,data,defaultExpireTime);
	}
	
	
	/**
	 * 获得缓存数据
	 * @param id
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Object id,Class<T> clazz){
		String key = id.toString() + clazz.getSimpleName();
		Jedis jedis = null;
		Object obj = null;
		try{
			jedis = jedispool.getJedis();
			String value = jedis.get(key.toString());
			if(!StringUtil.stringIsEmpty(value)){
				obj =  JSON.parseObject(value,clazz);
			}
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
		}finally{
			jedispool.returnResource(jedis);
		}
		return (T) obj;
	}
	
	/**
	 * 
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T> List<T> get(Object[] id ,Class<T> clazz){
		List<T> list = new ArrayList<T>();
		String[] parm = new String[id.length];
		for(int i=0;i<id.length;i++){
			parm[i] = id[i].toString() + clazz.getSimpleName();
		}
		Jedis jedis = null;
		try{
			jedis = jedispool.getJedis();
			List<String> values = jedis.mget(parm);
			for(String value : values){
			   if(!StringUtil.stringIsEmpty(value)){
				 list.add(JSON.parseObject(value,clazz));
			   }
			}
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
		}finally{
			jedispool.returnResource(jedis);
		}
		return list;
	}
	
	
	//放如map
	public boolean put(String key,Object field,Object data){
		Jedis jedis = null;
		boolean success = true;
		try{
			String value = JSON.toJSONString(data);
			jedis = jedispool.getJedis();
			jedis.hset(key, field.toString(), value);
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
			success = false;
		}finally{
			jedispool.returnResource(jedis);
		}
		return success;
	}
	
	//get from map
	@SuppressWarnings("unchecked")
	public <T> T get(String key,Object field,Class<T> clazz){
		Jedis jedis = null;
		Object obj = null;
		try{
			jedis = jedispool.getJedis();
			String value = jedis.hget(key, field.toString());
			if(!StringUtil.stringIsEmpty(value)){
				obj =  JSON.parseObject(value,clazz);
			}
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
		}finally{
			jedispool.returnResource(jedis);
		}
		return (T) obj;
	}
	
	public <T> List<T> get(String key,Object[] fields,Class<T> clazz){
		Jedis jedis = null;
		List<T> list = new ArrayList<T>();
		String[] parm = new String[fields.length];
		for(int i=0;i<fields.length;i++){
			parm[i] = fields[i].toString() + clazz.getSimpleName();
		}
		try{
			jedis = jedispool.getJedis();
			List<String> values = jedis.hmget(key, parm);
			for(String value : values){
				if(!StringUtil.stringIsEmpty(value)){
					list.add(JSON.parseObject(value,clazz));
				}
			}
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
		}finally{
			jedispool.returnResource(jedis);
		}
		return list;
	}
	
	//向有序集合中方数据
	public boolean zadd(String zset,double score,Object data){
		Jedis jedis = null;
		boolean success = true;
		try{
			String value = JSON.toJSONString(data);
			jedis = jedispool.getJedis();
			jedis.zadd(zset, score, value);
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
			success = false;
		}finally{
			jedispool.returnResource(jedis);
		}
		return success;
	}
	
	/**
	 * 获得某一数据在有序集合中的排名
	 * @param zset
	 * @param data
	 * @param desc 
	 * @return
	 */
	public Long zgetRranking(String zset,Object data,boolean desc){
		Long ranking = null;
		Jedis jedis = null;
		try{
			jedis = jedispool.getJedis();
			String value = JSON.toJSONString(data);
			if(desc){//
				ranking = jedis.zrevrank(zset, value);//从大到小
			}else{
				ranking = jedis.zrank(zset, value);//从小到大
			}
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
		}finally{
			jedispool.returnResource(jedis);
		}
		return ranking;
	}
	
	/**
	 * 获得有序集合某一排名范围内的数据
	 * @param zset
	 * @param start
	 * @param end
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getZsetRrank(String zset,int start,int end,Class<T> clazz,boolean desc){
		Jedis jedis = null;
		List<T> list = new ArrayList<T>(); 
		try{
			jedis = jedispool.getJedis();
			Set<String> set = new HashSet<String>();
			if(desc){//
			   set = jedis.zrevrange(zset, start, end);//从大到小
			}else{
			   set = jedis.zrange(zset, start, end);//从小到大
			}
	    	for(String value : set){
	    		list.add(JSON.parseObject(value, clazz));
	    	}
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
		}finally{
			jedispool.returnResource(jedis);
		}
		return list;
	}
	
	/**
	 * 删除rediskey
	 * @param key
	 * @return
	 */
	public boolean deleteKey(String key){
		Jedis jedis = null;
		boolean success = true;
		try{
			jedis = jedispool.getJedis();
			jedis.del(key);
		}catch(Exception e){
			logger.error("error message {}. error exception {}.",e.getMessage(), e);
			logger.error("error StackTrace ", e);
			success = false;
		}finally{
			jedispool.returnResource(jedis);
		}
		return success;
	}
	
}

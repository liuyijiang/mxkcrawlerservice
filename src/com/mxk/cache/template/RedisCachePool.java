package com.mxk.cache.template;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * redis 连接池
 * @author liuyijiang
 *
 */
public class RedisCachePool {

	private JedisPool jedisPool;
    
    public RedisCachePool(String host,int prot,int active,int maxIdle,int maxWait){
	     JedisPoolConfig config = new JedisPoolConfig();
	     config.setMaxActive(active);
	     config.setMaxIdle(maxIdle);
	     config.setMaxWait(maxWait);
	     config.setTestOnBorrow(true);
	     config.setTestOnReturn(true);
	     config.setTestWhileIdle(true);
	     config.setNumTestsPerEvictionRun(10);
	     jedisPool = new JedisPool(config, host, prot);        
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }
    
    public void returnResource(Jedis redis){
    	if (redis != null) {
    		jedisPool.returnResource(redis);
        }
    }
	
}

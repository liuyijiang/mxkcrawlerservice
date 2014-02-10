package com.mxk.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CacheService {

	public static Map<String,Object> map = new HashMap<String,Object>();
	
	public void set(String key,Object value){
		map.put(key, value);
	}
	
	public Object get(String key){
		return map.get(key);
	}
	

}

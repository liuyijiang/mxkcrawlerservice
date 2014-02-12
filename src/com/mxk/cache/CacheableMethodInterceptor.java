package com.mxk.cache;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mxk.cache.template.RedisCacheTemplate;
import com.mxk.web.user.UserPlus;
/**
 * 
 * @author liuyijiang
 *
 */
@Aspect
@Component
public class CacheableMethodInterceptor {
  
	public static final Logger logger = LoggerFactory.getLogger(CacheableMethodInterceptor.class); 
	
	@Autowired
	private RedisCacheTemplate redisCacheTemplate;
	
	@SuppressWarnings("unchecked")
	@Around(" @annotation(cacheable) ")
	public Object doCacheAction(ProceedingJoinPoint pjp,Cacheable cacheable) throws Throwable{
		Signature signature = pjp.getSignature();
	    Object obj = null;
		if(cacheable.usecache()){//使用缓存
			if(cacheable.cachetype().equals(CacheableType.CACHE_FOR_INSTER)) { //新增
				obj = pjp.proceed();
				if(cacheable.persist()){//需要持久化 放入redis
					persist(obj);
				}else{ //放入memcach
					
				}
			}else if(cacheable.cachetype().equals(CacheableType.CACHE_FOR_SELECT)){ //查询
				Object[] args = pjp.getArgs();//获得参数（key class）
				MethodSignature methodSignature = (MethodSignature) signature; 
				obj = get(args[0],methodSignature.getReturnType(),cacheable,pjp);
			}
		}else{
			obj = pjp.proceed();
		}
		return obj;
	    
	    
	}	
	
	
	@SuppressWarnings("unchecked")
	private <T> T get(Object id,Class<T> clazz,Cacheable cacheable,ProceedingJoinPoint pjp) throws Throwable {
		Object obj = redisCacheTemplate.get(id,clazz);
		if(obj == null){
			obj = pjp.proceed();
			if(obj != null && cacheable.recache()){
				if(cacheable.persist()){//需要持久化 放入redis
					persist(obj);
				}else{ //放入memcach
					
				}
			}
		}
		return (T) obj;
	}
	
    private void persist(Object obj) throws Exception {
    	if( obj != null ){
    		Field fid = obj.getClass().getSuperclass().getDeclaredField("id");//父类
    		fid.setAccessible(true);
    		Integer id = (Integer) fid.get(obj); //获得id
    		redisCacheTemplate.set(id, obj);
    	}
	}
    
//    public static void main(String[] args) throws Exception {
//		UserPlus plus = new UserPlus();
//		//User plus = new User();
//		plus.setId(12);
//		plus.setUserName("ok");
//		//Field fid = plus.getClass().getDeclaredField("id");//父类
//		Field fid = plus.getClass().getSuperclass().getDeclaredField("id");//父类
//		fid.setAccessible(true);
//		
//		
//		
//		System.out.println((Integer)fid.get(plus)); //获得id
//	}
    
    
//    private void memory(Object obj) {
//    	
//    }
	
}

package com.mxk.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {
  
	boolean usecache() default true;
	boolean persist() default false;
	boolean recache() default true;
	CacheableType cachetype() default CacheableType.CACHE_FOR_DELETE;
	int exptime() default 3000;
	
}

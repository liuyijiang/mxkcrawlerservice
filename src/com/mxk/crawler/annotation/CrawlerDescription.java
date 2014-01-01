package com.mxk.crawler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //@Target(ElementType.METHOD) 方法注解
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CrawlerDescription {
    
	String crawlerSite() default "";
	String crawlerMatchUrl();
	CrawleType crawlerType() default CrawleType.LINK;
	
}

package com.mxk.web.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //@Target(ElementType.TYPE) 类注解
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityDescription {
    
	boolean accredit() default false;
	boolean loginRequest() default false;
	
}

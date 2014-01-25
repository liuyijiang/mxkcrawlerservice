/**
 * 
 */
package com.mxk.web.base;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;

/**
 * @author 徐峰
 *
 */
public class MessageConverter implements HttpMessageConverter<Object> {
	static Logger logger = LoggerFactory.getLogger(MessageConverter.class);
	
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		//return (clazz == ViewResult.class || clazz == String.class);
		return true;
	}
	
	public final static List<MediaType> MediaTypeList = ImmutableList.of(MediaType.ALL);
	
	@Override
	public List<MediaType> getSupportedMediaTypes() {
//		logger.info("#messageConvert_track 打印调用堆栈 ", new Exception());
		return MediaTypeList;
	}

	@Override
	public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return null;
	}

	public final static String charset = "UTF-8";
	
	@Override
	public void write(Object t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		logger.debug("#out_write {}", t);
		outputMessage.getHeaders().setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		String s = JSON.toJSONString(t);
//		if (t instanceof ViewResult) {
//			s = ((ViewResult) t).json();
//		}
//		else if (t != null ) {
//			s = t.toString();
//		}
//		else {
//			s = "";
//		}
		FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(), charset));
	}

}

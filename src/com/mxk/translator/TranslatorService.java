package com.mxk.translator;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mxk.util.StringUtil;
/**
 * 简单翻译服务
 * @author Administrator
 *
 */
@Service
public class TranslatorService {

    public static final Logger logger = LoggerFactory.getLogger(TranslatorService.class);
	
    /** google翻译的http请求地址*/
	private static final String URL = "http://translate.google.cn/?tl=zh-CN&hl=";
	/** 编码集*/
	private static final String ENCODER = "UTF-8";
	/** html页面中中文位置*/
	private static final String RESULT = "span[id=result_box]";
	/** 去除html标签*/
	private static final String HTML = "<([^>]*)>";
	/** 匹配英文*/
	private static final String ENGLISH = "[^a-zA-Z]";
	
	/**
	 * 简单的基本翻译
	 * @param targetLan
	 * @param txt
	 * @return
	 */
	public String simpleTranslator(TranslatorType targetLan,String txt){
		HttpClient httpclient = new DefaultHttpClient();
		String result = "";
		try{
		    String translatorStr = URLEncoder.encode(txt,ENCODER);
		    HttpGet httpget = new HttpGet(URL + targetLan.getCode() + "&q=" + translatorStr);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String str = EntityUtils.toString(entity);
				Document doc = Jsoup.parse(str, ENCODER);
				result = regxp(doc.select(RESULT).html(),HTML,"");
			}
			httpget.abort();
		}catch(Exception e){
			logger.error("翻译字符串：{} 失败  txt:{}",e,txt);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 专业的翻译 翻译专业模型名词
	 * @param txt
	 * @return
	 */
	public String professionalTranslator(String txt){
		System.out.println(regxp(txt,ENGLISH,","));
		String str[] = regxp(txt,ENGLISH,",").split(",");
		List<String> list = new ArrayList<String>();
		for(String st : str){
			if(!StringUtil.stringIsEmpty(st)){
				list.add(st);
			}
		}
		for(String s : list){
			//redis获得专业数据
			if(s.toLowerCase().equals("askold")){
				txt = txt.replaceAll(s,"阿斯科尔德号");
			}
			if(s.toLowerCase().equals("cesarevic")){
				txt = txt.replaceAll(s,"太子号");
			}
			//System.out.println(s);
		}
		return txt;
	}
	
	/**
	 * 清理
	 * @param html
	 * @param regx
	 * @param split
	 * @return
	 */
	private String regxp(String html,String regx,String split){
    	Pattern patternHtml = Pattern.compile(regx);
    	Matcher matcherHtml = patternHtml.matcher(html);
    	StringBuffer sbHtml = new StringBuffer();
    	boolean result1 = matcherHtml.find();
    	while (result1) { //去除html
    	   matcherHtml.appendReplacement(sbHtml,split);
    	   result1 = matcherHtml.find();
    	}
    	matcherHtml.appendTail(sbHtml);
    	return sbHtml.toString();
    }
}

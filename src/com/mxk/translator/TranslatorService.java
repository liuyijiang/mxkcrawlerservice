package com.mxk.translator;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
/**
 * 简单翻译服务
 * @author Administrator
 *
 */
@Service
public class TranslatorService {

    public static final Logger logger = LoggerFactory.getLogger(TranslatorService.class);
	
    /** google翻译的http请求地址*/ 
    //http://translate.google.cn/#sv/zh-CN/M%C3%B6teskalendarium
	//private static final String URL = "http://translate.google.cn/?tl=zh-CN&hl=";
	private static final String URL = "http://translate.google.cn/";
	//private static final String RESLAN = "/zh-CN/";
	/** 编码集*/
	private static final String ENCODER = "UTF-8";
	/** html页面中中文位置*/
	private static final String RESULT = "span[id=result_box]";
	/** 去除html标签*/
	private static final String HTML = "<([^>]*)>";
//	/** 匹配英文*/
//	private static final String ENGLISH = "[^a-zA-Z]";
	
	private Map<String,String> professionalWord = new HashMap<String,String>();
	
	
	public TranslatorService(){
		initProfessionalWord();
	}
	
//	public static void main(String[] args) {
//		TranslatorService t = new TranslatorService();
//		t.;
//	}
	
	/**
	 * 将配置文件中那些专业名称加载带map中
	 */
	@SuppressWarnings("rawtypes")
	private void initProfessionalWord(){
		SAXReader reader = new SAXReader();
		try{
	    org.dom4j.Document document = reader.read(TranslatorService.class.getClassLoader().getResourceAsStream("professional_translator.xml"));
		Element root = document.getRootElement();
		Iterator it = root.elementIterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				professionalWord.put(element.attributeValue("id"),element.getText());
			}
		}catch(Exception e){
			logger.error("加载专业单词异常{}",e);
		}
	}
	
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
		    HttpGet httpget = new HttpGet(URL + targetLan.getCode() + "/zh-CN/" + translatorStr);
//		    HttpGet httpget = new HttpGet(URL + targetLan.getCode() + "&q=" + translatorStr);
//		    httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
//		    httpget.setHeader("Cookie","PREF=ID=9fc3c400c067c38c:U=96892e2b795b4eed:FF=0:NW=1:TM=1379168416:LM=1379168418:S=JkWheIuZQXbH4RIb; NID=67=o80SwOmx6y1ABmA2uPUWMEU4Q0-eXUBkubVeQKw3JHLXzDQ4OM4fJXk51qmqqn7h1TBbPsmNxmYz-YKjzaRQssyXCvNVU_JXogFmQLa8l-1rp1fwoqLINAtYtvln9lcK; _ga=GA1.4.2088469847.1389615985");
//		    httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		    httpget.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
//		    httpget.setHeader("Referer","http://translate.google.com.tw/?hl=zh-CN&tab=wT");
//		    httpget.setHeader("Host","translate.google.com.tw");
//		    httpget.setHeader("Content-Length","3554");
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
		return professionalWord.get(txt);
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

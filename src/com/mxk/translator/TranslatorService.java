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
	//private static final String URL = "http://translate.google.cn/?tl=";
    //http://translate.google.cn/#ru/zh-CN/
	private static final String URL = "http://translate.google.cn/?sl=";
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
		    String url = URL+ targetLan.getCode() + "&tl=zh-CN&q=" + translatorStr;
		    //String url = "http://translate.google.cn/?sl=ru&tl="+targetLan.getCode()+"&q=" + translatorStr;
		    HttpGet httpget = new HttpGet(url);
		   // System.out.println(url);
		    httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		    httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
		    httpget.setHeader("Cookie","	PREF=ID=9f1a0469aba5e7e0:U=1fcb2cf3664223f1:NW=1:TM=1378190176:LM=1378190186:S=efvQqjKbT1E9xhjA; NID=67=RROYHx6c5KEWNIp6aPpdlrtinakBmuj2g7t6ThxWXj4wZr-7TpDD1EPr5NllNTCpx-8Ib8RrsUdWoYBNMco0KxgfgNK71zps8p0Xh9Qy11w5CtlxgpbKrKJ4YHgdArGV; _ga=GA1.3.250260745.1389340620");
		    httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
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
	
	public static void main(String[] args) {
		TranslatorService s = new TranslatorService();
		String str = s.simpleTranslator(TranslatorType.CHINIA, "Ракетная установка SAM-2 Guideline Missile w/ Launcher, Trumpeter Models. По-русски ЗРК С-75М Волхов. Всё из коробки, только добавил таблички, сменил маркировку, а то китайцы чёрти-чё написали и заменил поручни на проволочные. Всё.");
		System.out.println(str);
	}
	
}

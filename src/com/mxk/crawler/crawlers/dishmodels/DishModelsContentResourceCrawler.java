package com.mxk.crawler.crawlers.dishmodels;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mxk.crawler.annotation.CrawleType;
import com.mxk.crawler.annotation.CrawlerDescription;
import com.mxk.crawler.base.Crawler;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Content;
import com.mxk.crawler.model.Links;
import com.mxk.translator.TranslatorService;
import com.mxk.translator.TranslatorType;
import com.mxk.util.StringUtil;

/**
 * 爬取 DishModels 帖子资源 罗斯网站 
 * 此网站比较特殊 爬取链接的时候就保存这个资源
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite = DishModelsContentResourceCrawler.SITE_NAME , crawlerType = CrawleType.CONTENT, crawlerMatchUrl = DishModelsContentResourceCrawler.MATCH_URL)
public class DishModelsContentResourceCrawler extends Crawler {

	@Value("${system.dishmodels.crawler.resource.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(DishModelsContentResourceCrawler.class);
	public static final String MATCH_URL = "http://www.dishmodels.ru/gshow.htm?p="; //http://www.dishmodels.ru/gshow.htm?p=13286&lng=E
	public static final String SITE_URL = "http://www.dishmodels.ru";
	public static final String SITE_NAME = "DishModels";
	
	public static void main(String[] args) {
		DishModelsContentResourceCrawler d = new DishModelsContentResourceCrawler();
		d.crawler("http://www.dishmodels.ru/gshow.htm?p=7265");
	}
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 DishModels 论坛帖子资源 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); //div class="dm_blockgshowT"
			//System.out.println(title);
			String str[] = doc.select("div[class=dm_blockgshowT]").select("b").first().html().split("<br />");
			Content content = new Content();
			content.setLikurl(url);
			content.setSitename(SITE_NAME);
			content.setSiteurl(SITE_URL);
			//content.set
			translator = new TranslatorService();
			String title = translator.simpleTranslator(TranslatorType.ENGLISH, str[0]);
			content.setHeadline(title + "(" + str[0] + ")");
			content.setMultiData(str[1]);
			String owner = translator.simpleTranslator(TranslatorType.RUSSIA, str[2]);
			content.setOwner(owner+ "("  + str[2] + ")");
			String info = translator.simpleTranslator(TranslatorType.RUSSIA,stringFormat(doc.select("p[align=justify]").first().html()));
			content.setInfo(info);
			content.setLikurl(url);
			content.setSitename(SITE_NAME);
			content.setSiteurl(SITE_URL);
			list.add(content);
		}catch(Exception e){
			logger.error("DishModels crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("DishModels 完成帖子资源爬取{},爬取帖子数量：{}",url, list.size());
		}
		return list;
	}
	
	/**
	 * 重写父类组装数据方法
	 */
	@Override
    public void packageData(Links link,Content resource){
		if(link.getMultiData() != null ){
			String[] str = link.getMultiData().split(",");
			resource.setHit(str[2]);
			resource.setPost(str[1]);
			resource.setSimpleImage(str[0]);
		}
	}
	
	@Override
	public boolean checkExecute() {
		return execute;
	}

	@Override
	public void setExecute(boolean runable) {
		this.execute = runable;
	}

	private String stringFormat(String str){
		Pattern patternHtml = Pattern.compile("<([^>]*)>");
    	Matcher matcherHtml = patternHtml.matcher(str);
    	StringBuffer sbHtml = new StringBuffer();
    	boolean result1 = matcherHtml.find();
    	while (result1) { //去除html
    	   matcherHtml.appendReplacement(sbHtml,"");
    	   result1 = matcherHtml.find();
    	}
    	matcherHtml.appendTail(sbHtml);
    	str = sbHtml.toString();
		String strs[] = str.split("&nbsp;");
		StringBuffer sb = new StringBuffer();
	    for(String st : strs){
	    	if(!StringUtil.stringIsEmpty(st) && !"\n".equals(st) && !"<br /> ".equals(st)){
	    		sb.append(st);
	    	}
	    }
	    //System.out.println(sb.toString());
	    return sb.toString().substring(0,350);//截取一部分 ;
	}
	
	
}

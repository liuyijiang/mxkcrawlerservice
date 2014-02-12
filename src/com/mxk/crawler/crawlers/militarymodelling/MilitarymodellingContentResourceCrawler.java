package com.mxk.crawler.crawlers.militarymodelling;

import java.util.ArrayList;
import java.util.List;

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
import com.mxk.crawler.crawlers.modelshipgallery.ModelShipGalleryContentResourceCrawler;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Content;
import com.mxk.crawler.model.Links;
import com.mxk.translator.TranslatorService;
import com.mxk.translator.TranslatorType;
import com.mxk.util.RegxpEnumUtil;
import com.mxk.util.StringUtil;

/**
 * 
 * @author Administrator 
 *  http://www.militarymodelling.com/news/browseModern.asp?at=59&atg=2&p=1
 */
@Service
@CrawlerDescription(crawlerSite= MilitarymodellingContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = MilitarymodellingContentResourceCrawler.MATCH_URL)
public class MilitarymodellingContentResourceCrawler extends Crawler {

	@Value("${system.militarymodelling.crawler.resource.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(ModelShipGalleryContentResourceCrawler.class);
	public static final String MATCH_URL = "http://www.militarymodelling.com/news/article";
	public static final String SITE_NAME = "MilitaryModelling";
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 MilitaryModelling 论坛帖子内容 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			//标题
			String title = doc.select("div[class=ArticleTitleBox]").select("h1").html();
			String auther1 = "";
			if(title.indexOf("'") != -1){//标题特殊处理
				auther1 = title.substring(0,title.indexOf("'"));
				title = title.substring(title.indexOf("'")+2);
				System.out.println(title);
			}
			String titleWord[] = title.split(" ");
			translator = new TranslatorService();
			StringBuilder sb = new StringBuilder();
			for(String str : titleWord){
				if(!StringUtil.stringIsEmpty(str) && str.matches(RegxpEnumUtil.ENGLISH_LOWER_UPPER_CASE.getCode())){ //是否是英语
					String tran = translator.professionalTranslator(str.toLowerCase());
					if(tran != null){
						sb.append(tran + " ");
					}else{
						sb.append(translator.simpleTranslator(TranslatorType.ENGLISH, str) + " ");
					}
				}else{
					sb.append(str + " ");
				}
			}
			//System.out.println(sb.toString());
			
			//System.out.println(title.substring(0,title.indexOf("'")));
			String auther = doc.select("div[class=Author]").html().toLowerCase();
			if(auther != null){
				auther = auther1 + "|" +auther.replace("by", "");
			}
			System.out.println(auther);
		}catch(Exception e){
			logger.error("MilitaryModelling crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME * 2);
			logger.info("MilitaryModelling 完成帖子爬取{},爬取link数量：{}",url, list.size());
		}
		return list;
	}
	
	@Override
	public boolean checkExecute() {
		return execute;
	}
	
	@Override
	public void setExecute(boolean runable) {
		this.execute = runable;
	}
	
	
}

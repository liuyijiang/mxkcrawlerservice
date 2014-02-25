package com.mxk.crawler.crawlers.arcair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mxk.crawler.annotation.CrawleType;
import com.mxk.crawler.annotation.CrawlerDescription;
import com.mxk.crawler.base.Crawler;
import com.mxk.crawler.crawlers.dishmodels.DishModelsLinkCrawler;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;

/**
 * 增量爬取器  爬取Arcair网站最新链接
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite = ArcairModelsSiteLinkCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = ArcairModelsSiteLinkCrawler.MATCH_LINK_URL)
public class ArcairModelsSiteLinkCrawler extends Crawler {

	@Value("${system.arcair.site.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(DishModelsLinkCrawler.class);
	public static final String MATCH_LINK_URL = "http://www.arcair.com/homepage-archive/";
	public static final String MATCH_CONTENT_URL = "http://www.arcair.com/";
	public static final String SITE_URL = "http://www.arcair.com/";
	public static final String SITE_NAME = "Arcair";
	
	public static void main(String[] args) {
		ArcairModelsSiteLinkCrawler a = new ArcairModelsSiteLinkCrawler();
		Links l = new Links();
		//http://www.arcair.com/
		//
		//l.setUrl("http://www.arcair.com/homepage-archive/2007/indexApr07.htm");
		//l.setUrl("http://www.arcair.com/homepage-archive/2007/indexJuly07.htm");
		l.setUrl("http://www.arcair.com/");
		a.crawler(l);
	}
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 Arcair 网站链接资源 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get();  //div dm_bodyblock
			Elements as = doc.select("a");
			Set<String> ulinkSet = new HashSet<String>(); // 去重复
			for (Element a : as) {
				String link = a.attr("href");
				if(!link.startsWith("http") && !link.contains("@") ){
					ulinkSet.add(link);
				}
			}
			for (String str : ulinkSet){
				if(str.startsWith("../../")){
					str = str.substring(6,str.length());
				}
				str = SITE_URL + str;//组合后的链接
				if(str.contains("homepage-archive")){
					Links link = new Links();
					link.setCreateTime(new Date());
					link.setUrl(str);
					link.setFromUrl(url);
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_LINK_URL); //匹配链接 strong
					list.add(link);
				} else {
					Links link = new Links();
					link.setCreateTime(new Date());
					link.setUrl(str);
					link.setFromUrl(url);
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_CONTENT_URL); //匹配链接 strong
					list.add(link);
				}
			}
		}catch(Exception e){
			logger.error("Arcair crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("Arcair 完成链接爬取{},爬取link数量：{}",url, list.size());
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

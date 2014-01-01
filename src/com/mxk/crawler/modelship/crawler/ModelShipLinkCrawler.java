package com.mxk.crawler.modelship.crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;

/**
 * 爬取钢铁浮城论坛资源 http://forum.modelship.com.tw  
 * @author Administrator  http://forum.modelship.com.tw/phpBB2/viewforum.php?f=3&topicdays=0&start=50
 * 初始link http://forum.modelship.com.tw/phpBB2/viewforum.php?f=3&topicdays=0&start=0
 * 初始匹配link http://forum.modelship.com.tw/phpBB2/viewforum.php 
 */
@Service
@CrawlerDescription(crawlerSite=ModelShipLinkCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = ModelShipLinkCrawler.MATCH_LINK_URL)
public class ModelShipLinkCrawler extends Crawler {

	@Value("${system.modelship.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(ModelShipLinkCrawler.class);
	
	public static final String MATCH_LINK_URL = "http://forum.modelship.com.tw/phpBB2/viewforum.php";
	public static final String MATCH_CONTENT_URL = "http://forum.modelship.com.tw/phpBB2/viewtopic.php";
	public static final String SITE_URL = "http://forum.modelship.com.tw/phpBB2/";
	public static final String SITE_NAME = "钢铁浮城";
	
	public static final String MODEL_SHIP_MATCH_LINK_URL = "viewforum.php?f=3&topicdays=0";
	//public static final String MODEL_SHIP_MATCH_CONTENT_URL = "viewforum.php";
	
	public static void main(String[] args) {
		ModelShipLinkCrawler m = new ModelShipLinkCrawler();
		m.crawler("http://forum.modelship.com.tw/phpBB2/viewforum.php?f=3&topicdays=0&start=0");
		//http://forum.modelship.com.tw/phpBB2/viewforum.php?f=3&topicdays=0&start=100
		//http://forum.modelship.com.tw/phpBB2/viewforum.php?f=3&amp;topicdays=0&amp;start=100&amp;sid=6c757ffc294b280e1d10e7fda6357e9e	
	}
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 钢铁浮城 论坛链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
			Document doc = conn.get();
			Elements as = doc.select("a[class=topictitle]");
			for(Element a : as){
		    	String alink = a.attr("href");
	    		Links link = new Links();
				link.setCreateTime(new Date());
				link.setUrl(SITE_URL + alink.substring(0,alink.lastIndexOf("&")));
				link.setFromUrl(url);
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setMatchUrl(MATCH_CONTENT_URL); //匹配链接
				list.add(link);
			    logger.info("钢铁浮城 爬取到链接：{}", link.getUrl());
			}
			Elements nav = doc.select("span[class=nav]").select("a");
		    for(Element a : nav){
		    	String alink = a.attr("href");
		    	if(alink.startsWith(MODEL_SHIP_MATCH_LINK_URL)){
		    		Links link = new Links();
					link.setCreateTime(new Date());
					link.setUrl(SITE_URL + alink.substring(0,alink.lastIndexOf("&")));
					link.setFromUrl(url);
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_LINK_URL); //匹配链接
					list.add(link);
				    logger.info("钢铁浮城 爬取到链接：{}", link.getUrl());
		    	}
		    }
		}catch(Exception e){
			logger.error("crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("钢铁浮城  完成链接爬取{},爬取link数量：{}",url, list.size());
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

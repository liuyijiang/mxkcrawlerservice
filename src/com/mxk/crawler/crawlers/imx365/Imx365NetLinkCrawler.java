package com.mxk.crawler.crawlers.imx365;

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
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;

/**
 * 爬取所有新帖子链接地址 max http://www.imx365.net/bbs/forum-7-457.html
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite="Imx365" , crawlerType=CrawleType.LINK, crawlerMatchUrl = Imx365NetLinkCrawler.MATCH_LINK_URL)
public class Imx365NetLinkCrawler extends Crawler {

	@Value("${system.imx365.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(Imx365NetLinkCrawler.class);
	public static final String MATCH_LINK_URL = "http://www.imx365.net/bbs/forum";
	public static final String MATCH_CONTENT_URL = "http://www.imx365.net/bbs/thread";
	public static final String SITE_URL = "http://www.imx365.net";
	
	/**
	 * 实现爬取数据
	 */
	@Override
	public List<Links> crawler(String url) {
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 Imx365 论坛链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get();
			Elements links = doc.select("a[class=title]");
			if(!links.isEmpty()){
				for(Element a : links){
					Links link = new Links();
					link.setCreateTime(new Date());
					link.setUrl(SITE_URL + a.attr("href"));
					link.setFromUrl(url);
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_CONTENT_URL); //匹配链接
					list.add(link);
					logger.info("Imx365 爬取到链接：{}", link.getUrl());
				}
				Elements parlinks = doc.select("div[class=page_bar]").select("a");
				for(Element ee : parlinks){
					Links link = new Links();
					link.setFromUrl(url);
					link.setCreateTime(new Date());
					link.setUrl(SITE_URL + ee.attr("href"));
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_LINK_URL);
					list.add(link);
					logger.info("Imx365 爬取到链接：{}", link.getUrl());
				}
			}
		}catch(Exception e){
			logger.error("Imx365 crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("Imx365 完成链接爬取{},爬取link数量：{}",url, list.size());
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

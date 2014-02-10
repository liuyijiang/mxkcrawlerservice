package com.mxk.crawler.crawlers.moxingnet;

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
 * 爬取 模型网 http://www.moxing.net/ 链接 
 * 开始页面  http://www.moxing.net/ bbs/forum-6-1.html
 * @author Administrator
 * 开始页面
 * http://www.moxing.net/bbs/forum-5-1.html
 * http://www.moxing.net/bbs/forum-6-1.html
 */
@Service
@CrawlerDescription(crawlerSite= MoxingnetLinkCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = MoxingnetLinkCrawler.MATCH_LINK_URL)
public class MoxingnetLinkCrawler extends Crawler {
  
	@Value("${system.moxingnet.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(MoxingnetLinkCrawler.class);
	public static final String START_LINK_URL = "http://www.moxing.net/bbs/forum-6-1.html";
	public static final String MATCH_LINK_URL = "http://www.moxing.net/bbs/forum";
	public static final String MATCH_CONTENT_URL = "http://www.moxing.net/bbs/thread";
	public static final String SITE_URL = "http://www.moxing.net/bbs/";
	public static final String SITE_NAME = "MoXingNet";
	
	public static void main(String[] args) {
		MoxingnetLinkCrawler m = new MoxingnetLinkCrawler();
		m.crawler("http://www.moxing.net/bbs/forum-6-1.html");
	}
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 MoXingNet 论坛链接资源 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get();  //div dm_bodyblock
			Elements tbody =  doc.select("tbody");
			int index = getStartIndex(url); // 开始位置
			for (int i = index ; i< tbody.size() ;i++) { //获取帖子链接
				Element tb = tbody.get(i);
				Links link = new Links();
				link.setCreateTime(new Date());
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setMatchUrl(MATCH_CONTENT_URL); //匹配链接
				link.setFromUrl(url);
				Element a = tb.select("th[class=subject common]").select("a").first();
				if(a == null){
					a = tb.select("th[class=subject new]").select("a").first();
				}
				if( a != null){
					link.setUrl(SITE_URL + a.attr("href"));
					String post = tb.select("td[class=nums]").select("strong").html();
					String hits = tb.select("td[class=nums]").select("em").html();
					link.setMultiData(post + "," + hits);
					list.add(link);
				}
			}
			Elements pagediv =  doc.select("div[class=pages]").first().select("a");
			for (Element a : pagediv) {
				Links link = new Links();
				link.setCreateTime(new Date());
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setMatchUrl(MATCH_LINK_URL); //匹配链接
				link.setUrl(SITE_URL + a.attr("href"));
				link.setFromUrl(url);
				list.add(link);
			}
		}catch(Exception e){
			logger.error("MoXingNet crawler error url: {} message :{}",url,e);
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("MoXingNet 完成链接爬取{},爬取link数量：{}",url, list.size());
		}
		return list;
	}
	
	/**
	 * 获得开始位置 解决论坛永久置顶的那么无用数据
	 * @return
	 */
	private int getStartIndex(String url){
		if(START_LINK_URL.equals(url)){
			return 5;
		}else{
			return 0;
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
	
}

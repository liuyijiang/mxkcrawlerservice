package com.mxk.crawler.crawlers.dbmodel;

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
 * 爬取 链接资源 东北模型论坛
 * @author Administrator
 * http://bbs.dbmodel.com/forumdisplay.php?fid=29 手办
 * http://bbs.dbmodel.com/forumdisplay.php?fid=74 军模
 * http://bbs.dbmodel.com/forumdisplay.php?fid=5 国外经典
 *
 */
@Service
@CrawlerDescription(crawlerSite = DbmodelLinkCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = DbmodelLinkCrawler.MATCH_LINK_URL)
public class DbmodelLinkCrawler extends Crawler {

	@Value("${system.dbmodel.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(DbmodelLinkCrawler.class);
	public static final String MATCH_LINK_URL = "http://bbs.dbmodel.com/forumdisplay.php";
	public static final String MATCH_CONTENT_URL = "http://bbs.dbmodel.com/viewthread.php";
	public static final String SITE_NAME = "东北模型论坛";
	public static final String SITE_URL = "http://bbs.dbmodel.com/";
	
//	public static void main(String[] args) {
//		DbmodelLinkCrawler d = new DbmodelLinkCrawler();
//		Links l = new Links();
//		l.setUrl("http://bbs.dbmodel.com/forumdisplay.php?fid=5&page=1");
//		d.crawler(l);
//		//d.crawler("http://bbs.dbmodel.com/forumdisplay.php?fid=5&page=1");
//	}
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取东北模型论坛 论坛链接资源 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get();  //div dm_bodyblock
			Elements tbodys = doc.select("tbody");
			for(Element tbody : tbodys){
				Element a = tbody.select("th[class=subject common").select("a").first();
				if(a != null){
					Links link = new Links();
					link.setCreateTime(new Date());
					link.setUrl(SITE_URL + a.attr("href"));
					link.setFromUrl(url);
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_CONTENT_URL); //匹配链接 strong
					String hits = tbody.select("td[class=nums").select("em").html();
					String post = tbody.select("td[class=nums").select("strong").html();
					link.setMultiData(hits+","+post);
					list.add(link);
				}
			}
			Elements pages = doc.select("div[class=pages]").first().select("a");
			for (Element page : pages) {
				Links link = new Links();
				link.setCreateTime(new Date());
				link.setUrl(SITE_URL + page.attr("href"));
				link.setFromUrl(url);
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setMatchUrl(MATCH_LINK_URL); //匹配链接
				list.add(link);
			}
		}catch(Exception e){
			logger.error("东北模型论坛 crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("东北模型论坛 完成链接爬取{},爬取link数量：{}",url, list.size());
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

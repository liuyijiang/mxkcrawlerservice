package com.mxk.crawler.crawlers.militarymodelling;

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
import com.mxk.crawler.crawlers.modelshipgallery.ModelShipGalleryContentResourceCrawler;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;

/**
 * 爬取militarymodelling完整链接类容 国外网站
 * @author Administrator 
 * http://www.militarymodelling.com/news/browseModern.asp?at=59&atg=2&p=1
 */
@Service
@CrawlerDescription(crawlerSite= MilitarymodellingContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = MilitarymodellingLinkCrawler.MATCH_LINK_URL)
public class MilitarymodellingLinkCrawler extends Crawler {

	@Value("${system.militarymodelling.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(ModelShipGalleryContentResourceCrawler.class);
	public static final String MATCH_LINK_URL = "http://www.militarymodelling.com/forums/threads.asp";
	public static final String SITE_NAME = "MilitaryModelling";
	public static final String MATCH_CONTENT_URL = "http://www.militarymodelling.com/forums/postings.asp";
	public static final String SITE_URL = "http://www.militarymodelling.com";
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 modelshipgallery 论坛帖子 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT*10);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			Elements trs = doc.select("table[class=StdText]").select("tr[class=RowStyleAlt]");
			for(Element tr : trs){
				Elements tds = tr.select("td");
				Links link = new Links();
				link.setCreateTime(new Date());
				String rurl = SITE_URL + tds.get(1).select("a").first().attr("href");
				String post =  tds.get(2).html();
				String hits = tds.get(3).html();
				link.setUrl(rurl);
				link.setMultiData(hits+","+post);
				link.setFromUrl(url);
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setMatchUrl(MATCH_CONTENT_URL); //匹配链接
				list.add(link);
				logger.info("modelshipgallery 爬取到链接：{}", link.getUrl());
			}
			Elements navs = doc.select("div[class=NavigationContainer]").select("a");
			if(!navs.isEmpty()){
				for(Element a : navs){
					Links link = new Links();
					link.setCreateTime(new Date());
					link.setUrl(SITE_URL + "/forums/" + a.attr("href"));
					link.setFromUrl(url);
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_LINK_URL); //匹配链接
					list.add(link);
					logger.info("modelshipgallery 爬取到链接：{}", link.getUrl());
				}
			}	
		}catch(Exception e){
			logger.error("MilitaryModelling crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("MilitaryModelling 完成链接爬取{},爬取link数量：{}",url, list.size());
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

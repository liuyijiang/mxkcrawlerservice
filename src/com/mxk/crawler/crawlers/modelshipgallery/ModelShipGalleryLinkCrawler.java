package com.mxk.crawler.crawlers.modelshipgallery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

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
 * 爬取所有新链接地址 http://www.modelshipgallery.com
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite= ModelShipGalleryLinkCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = ModelShipGalleryLinkCrawler.MATCH_LINK_URL)
public class ModelShipGalleryLinkCrawler extends Crawler {

	@Value("${system.modelshipgallery.crawler.link.execute}")
	private boolean execute;
	
	@Value("${system.modelshipgallery.crawler.link.init}")
	private boolean init;
	
	public static final Logger logger = LoggerFactory.getLogger(ModelShipGalleryLinkCrawler.class);
	public static final String MATCH_LINK_URL = "http://www.modelshipgallery.com/gallery/main";
	public static final String MATCH_CONTENT_URL = "http://www.modelshipgallery.com/gallery/";
	public static final String SITE_URL = "http://www.modelshipgallery.com/gallery/";
	public static final String SITE_NAME = "modelshipgallery";
	public static final String HEADER_PAGE = "http://www.modelshipgallery.com/gallery/gallery-header.html";
	
//	public static void main(String[] args) {
//		ModelShipGalleryLinkCrawler m = new ModelShipGalleryLinkCrawler();
//		m.crawler("http://www.modelshipgallery.com/gallery/main-02.html");
//		//m.initLink();
//	}
	
	/**
	 * http://www.modelshipgallery.com/gallery/gallery-header.html
	 * 此网站比较特殊 在服务初始化的时候就将所有的初始内容页面爬取下来 modelshipgallery header page
	 */
	@PostConstruct 
	public void crawlerPagings(){
		if(init){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					initLink();
				}
				
			}).start();
		}
	}
	
	/**
	 * 初始化爬取的根页面链接
	 */
	private void initLink(){
		logger.info("开始爬取 modelshipgallery header page", HEADER_PAGE);
		try{
			List<Links> list = new ArrayList<Links>();
			Connection conn = Jsoup.connect(HEADER_PAGE);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			Elements links = doc.select("a");
			for (Element a : links) {
				Links link = new Links();
				link.setCreateTime(new Date());
				link.setFromUrl(HEADER_PAGE);
				link.setMatchUrl(MATCH_LINK_URL);
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setUrl(SITE_URL+a.attr("href"));
				list.add(link);
			}
			logger.info("爬取 modelshipgallery header page 总页数：{}", list.size());
			crawlerService.saveLink(list);
		}catch(Exception e){
			logger.error("modelshipgallery crawler error url: {} message :{}",HEADER_PAGE,e.getMessage());
		}
	}
	
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 modelshipgallery 论坛链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get();
			//System.out.println(doc);
			Elements links = doc.select("a");
			for (Element a : links) {
				Links link = new Links();
				link.setCreateTime(new Date());
				link.setFromUrl(url);
				link.setMatchUrl(MATCH_CONTENT_URL);
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setUrl(SITE_URL+a.attr("href"));
				list.add(link);
			}
		}catch(Exception e){
			logger.error("modelshipgallery crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("modelshipgallery 完成链接爬取{},爬取link数量：{}",url, list.size());
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

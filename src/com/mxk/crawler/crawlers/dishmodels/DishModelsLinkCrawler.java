package com.mxk.crawler.crawlers.dishmodels;

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
 * 爬取 DishModels 帖子资源 罗斯网站 
 * 此网站比较特殊 爬取链接的时候就保存这个资源
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite="DishModels" , crawlerType=CrawleType.LINK, crawlerMatchUrl = DishModelsLinkCrawler.MATCH_LINK_URL)
public class DishModelsLinkCrawler extends Crawler {
   
	@Value("${system.dishmodels.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(DishModelsLinkCrawler.class);
	public static final String MATCH_LINK_URL = "http://www.dishmodels.ru/glr_main.htm";
	public static final String MATCH_CONTENT_URL = "http://www.dishmodels.ru/gshow.htm?p=";
	public static final String SITE_URL = "http://www.dishmodels.ru";
	
	public static void main(String[] args) {
		DishModelsLinkCrawler d = new DishModelsLinkCrawler();
		d.crawler("http://www.dishmodels.ru/glr_main.htm?p=&lng=E&np=806"); //494
	}
	
	/**
	 * 此网站比较特殊 在服务初始化的时候就将所有的分页link计算出来并保存保存
	 */
	@PostConstruct 
	public void crawlerPagings(){
		if(!execute){
			logger.info("初始化 DishModels 网站分页后link 初始页面 {}", MATCH_LINK_URL);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					logger.info("开始爬取 DishModels 总页数：{}", MATCH_LINK_URL);
					try{
						List<Links> list = new ArrayList<Links>();
						Connection conn = Jsoup.connect(MATCH_LINK_URL);
						conn.timeout(TIME_OUT);
						conn.userAgent(USERAGENT);
						Document doc = conn.get(); 
						String index = doc.select("a[class=dm_popupctrl]").html();
						int page = Integer.parseInt(index.substring(index.lastIndexOf(" ")+1,index.length()));
						//http://www.dishmodels.ru/glr_main.htm?p=&np=24 分页url
						for (int i=1; i<= page; i++) {
							Links link = new Links();
							link.setCreateTime(new Date());
							link.setFromUrl(MATCH_LINK_URL);
							link.setMatchUrl(MATCH_LINK_URL);
							link.setState(ResourceState.NO_CRAWLER.getCode());
							link.setUrl("http://www.dishmodels.ru/glr_main.htm?p=&lng=E&np="+i);
							list.add(link);
						}
						logger.info("爬取 DishModels 分页后link总页数：{}", list.size());
						crawlerService.saveLink(list);
					}catch(Exception e){
						logger.error("DishModels crawler error url: {} message :{}",MATCH_LINK_URL,e.getMessage());
					}
				}
				
			}).start();
		}
		
	}
	
	/**
	 * 爬取数据
	 */
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 DishModels 论坛链接和帖子资源 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get();  //div dm_bodyblock
			Elements div =  doc.select("div[class=dm_bodyblock]");
			for(Element el : div){
				String smallimage = el.select("img").attr("src");
				Elements bs = el.select("b");
				String comments = bs.get(7).html();
				String hits = bs.get(6).html();
				Links link = new Links();
				link.setCreateTime(new Date());
				link.setUrl(SITE_URL + el.select("a").attr("href"));
				link.setFromUrl(url);
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setMatchUrl(MATCH_CONTENT_URL); //匹配链接
				link.setMultiData(SITE_URL+smallimage +"," + comments + ","+ hits);
				list.add(link);
			}
		}catch(Exception e){
			logger.error("DishModels crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("DishModels 完成链接爬取{},爬取link数量：{}",url, list.size());
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

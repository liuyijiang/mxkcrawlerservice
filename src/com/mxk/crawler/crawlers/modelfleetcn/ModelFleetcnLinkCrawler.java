package com.mxk.crawler.crawlers.modelfleetcn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
 * 爬取海权无谓论坛所有资源链接
 * http://bbs.modelfleetcn.com/forum.php?mod=forumdisplay&fid=2
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite = ModelFleetcnLinkCrawler.SITE_NAME, crawlerType=CrawleType.LINK, crawlerMatchUrl = ModelFleetcnLinkCrawler.MATCH_LINK_URL)
public class ModelFleetcnLinkCrawler extends Crawler {

	@Value("${system.modelfleetcn.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(ModelFleetcnLinkCrawler.class);
	public static final String MATCH_LINK_URL = "http://bbs.modelfleetcn.com/forum.php?mod=forumdisplay&fid=2&page=";
	public static final String MATCH_CONTENT_URL = "http://bbs.modelfleetcn.com/forum.php?mod=viewthread"; //帖子的链接
	public static final String SITE_URL = "http://bbs.modelfleetcn.com/";
	public static final String SITE_NAME = "海权无谓";
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 海权无谓 论坛链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			Document doc = conn.get();
			Elements links = doc.select("a[class=xst]");//bm bw0 pgs cl 	div:not(.logo) 表示不包含 class=logo 元素的所有 div 列表 
			if(!links.isEmpty()){ //获取帖子链接
				for(Element a : links){
					Links link = new Links();
					link.setCreateTime(new Date());
					link.setUrl(SITE_URL + a.attr("href"));
					link.setFromUrl(url);
					link.setMatchUrl(MATCH_CONTENT_URL);
					link.setState(ResourceState.NO_CRAWLER.getCode());
					list.add(link);
					logger.info("爬取到 海权无谓 链接：{}", link.getUrl());
				}
				Elements parlinks = doc.select("div[class=bm bw0 pgs cl]").select("a");
				for(Element ee : parlinks){ //获取页面链接
					if(isMatch(SITE_URL + ee.attr("href"))){
						Links link = new Links();
						link.setFromUrl(url);
						link.setCreateTime(new Date());
						link.setUrl(SITE_URL + ee.attr("href"));
						link.setState(ResourceState.NO_CRAWLER.getCode());
						list.add(link);
						link.setMatchUrl(MATCH_LINK_URL);
						logger.info("爬取到海权无谓 链接：{}", link.getUrl());
					}
				}
			}
		}catch(Exception e){
			logger.error("海权无谓 crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("完成 海权无谓  链接爬取{},爬取link数量：{}",url, list.size());
		}
		return list;
	}

	public boolean isMatch(String url) {
		if(StringUtils.isNotEmpty(url) && url.startsWith(MATCH_LINK_URL)){
			return true;
		}else{
			return false;
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

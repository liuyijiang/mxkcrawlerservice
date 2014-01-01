package com.mxk.crawler.xiaot.crawler;

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
 * 爬取 xiaot模型空间站 谓论坛链接资源 http://www.xiaot.com
 * @author Administrator  bbs.xiaot.com/forum.php?mod=forumdisplay
 *http://bbs.xiaot.com/forum.php?mod=forumdisplay&fid=92&filter=typeid&typeid=115  forum.php?mod=viewthread&amp;tid=936932& forum.php?mod=viewthread&amp;
 */
@Service
@CrawlerDescription(crawlerSite = XiaoTLinkCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = XiaoTLinkCrawler.MATCH_LINK_URL)
public class XiaoTLinkCrawler extends Crawler {

	@Value("${system.xiaot.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(XiaoTLinkCrawler.class);
	public static final String MATCH_LINK_URL = "http://bbs.xiaot.com/forum.php?mod=forumdisplay";
	public static final String MATCH_CONTENT_URL = "http://bbs.xiaot.com/forum.php?mod=viewthread";
	public static final String SITE_URL = " http://www.xiaot.com/";
	public static final String SITE_NAME = "xiaot模型空间站";
  
	public static final String XIAO_T_MATCH_LINK_URL = "forum.php?mod=forumdisplay";
	public static final String XIAO_TMATCH_CONTENT_URL = "forum.php?mod=viewthread";
	
//	public static void main(String[] args) {
//		XiaoTLinkCrawler c = new XiaoTLinkCrawler();
//		c.crawler("http://bbs.xiaot.com/forum.php?mod=forumdisplay&fid=92&typeid=115&filter=typeid&typeid=115&page=2");
//	    String url = "http://www.xiaot.com/forum.php?mod=viewthread&tid=932281&extra=page%3D2%26filter%3Dtypeid%26typeid%3D115%26typeid%3D115&mobile=2";
//	    String n = url.replaceAll("&mobile=2", "");
//	    System.out.println(n);
//	
//	}
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 xiaot模型空间站 论坛链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			Document doc = conn.get();
			Elements as = doc.select("a"); //<th class="common">s xst th[class=common]
			for(Element a : as){
				String links = a.attr("href");
				if(links.startsWith(XIAO_TMATCH_CONTENT_URL)){
					Links link = new Links();
					link.setFromUrl(url);
					link.setCreateTime(new Date());
					link.setUrl(SITE_URL + links.replaceAll("&mobile=2", "") );
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_CONTENT_URL);
					list.add(link);
					logger.info("爬取到xiaot模型空间站 帖子链接：{}", link.getUrl());
				}else if(links.startsWith(XIAO_T_MATCH_LINK_URL)){
					Links link = new Links();
					link.setFromUrl(url);
					link.setCreateTime(new Date());
					link.setUrl(SITE_URL + links.replaceAll("&mobile=2", ""));
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_LINK_URL);
					list.add(link);
					logger.info("爬取到xiaot模型空间站链接：{}", link.getUrl());
				}
			}
		}catch(Exception e){
			logger.error("crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("xiaot模型空间站 完成链接爬取{},爬取link数量：{}",url, list.size());
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

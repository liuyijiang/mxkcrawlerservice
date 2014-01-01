package com.mxk.crawler.imx365.crawler;


import java.util.ArrayList;
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
import com.mxk.crawler.model.Content;
import com.mxk.util.StringUtil;

/**
 * Imx365 爬取所有新帖子内容地址 max http://www.imx365.net/bbs/thread
 * 没有资源 http://www.imx365.net/bbs/thread-4-166872-1-1.html
 * http://www.imx365.net/bbs/thread-164514-1-1.html
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite="Imx365" , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = "http://www.imx365.net/bbs/thread")
public class Imx365NetContentResourceCrawler extends Crawler {

	@Value("${system.imx365.crawler.resource.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(Imx365NetContentResourceCrawler.class);
	public static final String MATCH_URL = "http://www.imx365.net/bbs/thread";
	public static final String SITE_URL = "http://www.imx365.net";
	public static final String SITE_NAME = "Imx365";
	public static final String SITE_IMAGE = "http://s.imx365.net";
	
	public static void main(String[] args) {
		Imx365NetContentResourceCrawler c = new Imx365NetContentResourceCrawler();
		c.crawler("http://www.imx365.net/bbs/thread-7-203858-1-1.html");
	}
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 Imx365 论坛链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
			Document doc = conn.get(); 
			//System.out.println(doc);
			Elements imgs =  doc.select("div[id=firstpost]").select("img");
			System.out.println(imgs);
			if(!imgs.isEmpty()){
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				int index = 1;
				List<String> images = new ArrayList<String>();
				for(Element img : imgs){
					if(index <= CRAWLER_IMAGE_COUNT){
						String url1 = img.attr("rel");
						String url2 = img.attr("src");
						if(url1 != null && url1.startsWith(SITE_IMAGE)){
							images.add(url1);
							 index ++ ;
						}else if(url2 != null){
							images.add(url2);
							 index ++ ;
						}
					}else{
						break;
					}
				}
				content.setImages(images);
				Element e3 =  doc.select("span[class=numeric]").get(1);
				content.setPost(e3.html());//评论数量
				Elements e1 =  doc.select("div[class=post-heading]");
				Elements e2 =  doc.select("div[class=div_author_title]");
				content.setOwner(e2.first().select("a").html());
				content.setHeadline(e1.select("h2").html());
				Elements comments =  doc.select("div[id=firstpost]");
				StringBuffer sb = new StringBuffer();
				for(int i=1;i<comments.size();i++){
					sb.append(comments.get(i).html());
				}
				content.setComment(StringUtil.regxpForSymbol(StringUtil.regxpForHtml(sb.toString())));
				list.add(content);
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

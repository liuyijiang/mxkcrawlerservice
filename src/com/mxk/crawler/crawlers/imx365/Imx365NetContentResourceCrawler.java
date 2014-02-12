package com.mxk.crawler.crawlers.imx365;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.mxk.crawler.base.SiteInfo;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Content;
import com.mxk.crawler.model.Links;
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
		//c.crawler("http://www.imx365.net/bbs/thread-7-165084-1-18.html");
	}
	
	@Override
	public List<? extends BaseResource> crawler(Links link) {
		String url = link.getUrl();
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 Imx365 论坛帖子内容 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			Elements imgs =  doc.select("div[id=firstpost]").select("img");
//			System.out.println(imgs);
			if(!imgs.isEmpty() && imgs.size() > 3){ //爬取至少有3张图片的帖子
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				List<String> images = new ArrayList<String>();
				content.setImages(images);
				int index = 1;
				for (Element img : imgs) {
					String url1 = img.attr("rel");
					String url2 = img.attr("src");
					if(url1 != null && url1.startsWith(SITE_IMAGE)){
						images.add(url1);
						index++;
					}else if(url2 != null && url2.startsWith(SITE_IMAGE)){
						images.add(url2);
						index++;
					}
					if(index >= 5 ){
						break;
					}
				}
				content.setSimpleImage(images.get(0));
//				int index = 1;
//				List<String> images = new ArrayList<String>();
//				for(Element img : imgs){
//					if(index <= CRAWLER_IMAGE_COUNT){
//						String url1 = img.attr("rel");
//						String url2 = img.attr("src");
//						if(url1 != null && url1.startsWith(SITE_IMAGE)){
//							images.add(url1);
//							 index ++ ;
//						}else if(url2 != null){
//							images.add(url2);
//							 index ++ ;
//						}
//					}else{
//						break;
//					}
//				}
//				content.setImages(images);
				content.setMultiData(SiteInfo.CHINIA.getCode());
				Elements e3 =  doc.select("span[class=numeric]");
				content.setPost(e3.get(1).html());//评论数量
				content.setHit(e3.get(0).html());
				
				String info = doc.select("div[id=firstpost]").html();
				Pattern p = Pattern.compile("\n");//去掉换行
			    Matcher m = p.matcher(info.trim());
			    info = StringUtil.regxpForHtml(m.replaceAll(""),"");
				String remove = info.substring(info.indexOf("KBytes"),info.lastIndexOf("KBytes"));
				info = info.replace(remove, "");//替换\n  2013-12-29 05:35:00
				content.setInfo(info);
				Elements e1 =  doc.select("div[class=post-heading]");
				Elements e2 =  doc.select("div[class=div_author_title]");
				content.setOwner(e2.first().select("a").html());
				content.setHeadline(e1.select("h2").html());
				list.add(content);
			}
		}catch(Exception e){
			logger.error("Imx365 crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("Imx365 完成帖子爬取{},爬取link数量：{}",url, list.size());
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

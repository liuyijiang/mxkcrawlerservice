package com.mxk.crawler.crawlers.moxingnet;

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
import com.mxk.crawler.model.Links;
import com.mxk.util.StringUtil;
/**
 * http://www.moxing.net/bbs/thread-6372-1-90.html
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite= MoxingnetContentResourceCrawler.SITE_NAME , crawlerType = CrawleType.CONTENT, crawlerMatchUrl = MoxingnetContentResourceCrawler.MATCH_URL)
public class MoxingnetContentResourceCrawler extends Crawler {

	@Value("${system.moxingnet.crawler.resource.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(MoxingnetContentResourceCrawler.class);
	public static final String MATCH_URL = "http://www.moxing.net/bbs/thread";
	public static final String SITE_URL = "http://www.moxing.net/";
	public static final String SITE_NAME = "MoXingNet";
	public static final String IMAGE_URL = "http://www.moxing.net/bbs/";
	
	public static void main(String[] args) {
		MoxingnetContentResourceCrawler m = new MoxingnetContentResourceCrawler();
		m.crawler("http://www.moxing.net/bbs/thread-45903-1-5.html");
		
	}
	
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 MoXingNet 论坛帖子 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			Elements imgs = doc.select("div[class=t_msgfontfix]").select("img"); //获得图片
			if(imgs.size() >= 3){ //至少3张图片
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				content.setLikurl(url);
				for (Element img : imgs) {
					String file = img.attr("file");
					String src = img.attr("src");
					if(!StringUtil.stringIsEmpty(file) && !StringUtil.getFileSuffixName(file).equals("gif")){
						content.setSimpleImage(IMAGE_URL + file);
						break;
					}
					if(!StringUtil.stringIsEmpty(src) && !StringUtil.getFileSuffixName(src).equals("gif")){
						content.setSimpleImage(src);
						break;
					}
				}
				content.setOwner(doc.select("div[class=postinfo]").first().select("a").html());
				content.setHeadline(doc.select("div[id=threadtitle]").first().select("h1").html());
				String info = trimString(doc.select("div[class=t_msgfontfix]").select("tr").html()); 
				String remove = info.substring(info.indexOf("\n"),info.lastIndexOf("\n"));
				info = info.replace(remove, "");//替换\n  2013-12-29 05:35:00
				content.setInfo(info);
				list.add(content);
			}
		}catch(Exception e){
			logger.error("MoXingNet crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("MoXingNet 完成帖子爬取{},爬取link数量：{}",url, list.size());
		}
		return list;
	}
	
	/**
	 * 重写父类组装数据方法
	 */
	@Override
    public void packageData(Links link,Content resource){
		if(link.getMultiData() != null ){
			String[] str = link.getMultiData().split(",");
			resource.setHit(str[1]);
			resource.setPost(str[0]);
		}
	}
	
	
	private String trimString(String str){
		String[] strs = StringUtil.regxpForHtml(str,",").split(",");
		StringBuffer sb = new StringBuffer();
		for (String ss : strs) {
			if(!StringUtil.stringIsEmpty(ss)){
				sb.append(ss);
			}
		}
		return sb.toString();
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

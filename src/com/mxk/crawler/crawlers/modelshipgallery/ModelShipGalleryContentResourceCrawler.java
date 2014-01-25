package com.mxk.crawler.crawlers.modelshipgallery;

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
import com.mxk.crawler.base.SiteInfo;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Content;
import com.mxk.translator.TranslatorService;
import com.mxk.translator.TranslatorType;
import com.mxk.util.StringUtil;

/**
 * 
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite= ModelShipGalleryContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = ModelShipGalleryContentResourceCrawler.MATCH_URL)
public class ModelShipGalleryContentResourceCrawler extends Crawler {

	@Value("${system.modelshipgallery.crawler.resource.execute}")
	private boolean execute;
	
	
	public static final Logger logger = LoggerFactory.getLogger(ModelShipGalleryContentResourceCrawler.class);
	public static final String MATCH_URL = "http://www.modelshipgallery.com/gallery/";
	public static final String SITE_NAME = "modelshipgallery";
	
	public static void main(String[] args) {
		ModelShipGalleryContentResourceCrawler m = new ModelShipGalleryContentResourceCrawler();
		m.crawler("http://www.modelshipgallery.com/gallery/ca/sms/Scharnhorst-350-ra/index.htm");
	}
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 modelshipgallery 论坛帖子 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(getContextUrl(url));
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			//System.out.println(doc);
			Elements font = doc.select("font");
			Content content = new Content();
			content.setLikurl(url);
			content.setSitename(SITE_NAME);
			content.setSiteurl(MATCH_URL);
			//translator = new TranslatorService();
			//System.out.println(font.get(1).html());
			StringBuffer sb = new StringBuffer();
			String titleWord[] = font.get(1).html().split(" ");
			//题目翻译 这个很重要
			for(String str : titleWord){
				if(str.matches("^[a-zA-Z]*")){
					String tran = translator.professionalTranslator(str.toLowerCase());
					if(tran != null){
						sb.append(tran+" ");
					}else{
						sb.append(translator.simpleTranslator(TranslatorType.ENGLISH, str) + " ");
					}
				}else{
					sb.append(str + " ");
				}
			}
			//标题
			String title = sb.toString();
			content.setHeadline(title);
			String owner = font.get(3).html();
			//作者
			content.setOwner(translator.simpleTranslator(TranslatorType.ENGLISH, owner.toLowerCase().replace("by", ""))+ "|" + owner);
			//图片
			content.setSimpleImage(url.substring(0,url.lastIndexOf("/")) + "/"+doc.select("img").first().attr("src"));
			Elements ps = doc.select("p");
			StringBuffer sbinfo = new StringBuffer();
			for(Element p : ps){
				String strs[] = StringUtil.regxpForHtml(p.html(),",").split(",");
				for(String str:strs){
					if(!StringUtil.stringIsEmpty(str)){
						sbinfo.append(translator.simpleTranslator(TranslatorType.ENGLISH,str));
					}
				}
			}
			//内容
			content.setComment(title + sbinfo.toString());
			content.setMultiData(SiteInfo.EUROPE.getCode());
		}catch(Exception e){
			logger.error("modelshipgallery crawler error url: {} message :{}",url,e);
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("modelshipgallery 完成帖子爬取{},爬取link数量：{}",url, list.size());
		}
		return list;
	}
	
	/**
	 * 获取内容链接 此网站使用的是iframe的方式
	 * @param url
	 * @return
	 * @throws Exception 
	 */
	private String getContextUrl(String url) throws Exception{
		String rurl="";
		Connection conn = Jsoup.connect(url);
		conn.timeout(TIME_OUT);
		conn.userAgent(USERAGENT);
		Document doc = conn.get(); 
		Elements framemain = doc.select("frame[name=main]");
		Elements rightFrame = doc.select("frame[name=RightFrame]");
		if(framemain.attr("src") != null){
			rurl = framemain.attr("src");
		}
		if(rightFrame.attr("src") != null){
			rurl = rightFrame.attr("src");
		}
		System.out.println(url.substring(0,url.lastIndexOf("/")) + "/" + rurl);
		return url.substring(0,url.lastIndexOf("/")) + "/" + rurl;
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

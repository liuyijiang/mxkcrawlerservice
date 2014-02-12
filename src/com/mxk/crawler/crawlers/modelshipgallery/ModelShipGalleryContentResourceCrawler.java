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
import com.mxk.crawler.model.Links;
import com.mxk.translator.TranslatorService;
import com.mxk.translator.TranslatorType;
import com.mxk.util.StringUtil;

/**
 * 
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite= ModelShipGalleryContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = ModelShipGalleryContentResourceCrawler.MATCH_URL)
public class ModelShipGalleryContentResourceCrawler extends Crawler {

	@Value("${system.modelshipgallery.crawler.resource.execute}")
	private boolean execute;
	
	
	public static final Logger logger = LoggerFactory.getLogger(ModelShipGalleryContentResourceCrawler.class);
	public static final String MATCH_URL = "http://www.modelshipgallery.com/gallery/";
	public static final String SITE_NAME = "modelshipgallery";
	
	public static void main(String[] args) {
		//error http://www.modelshipgallery.com/gallery/dd/dd-245/350-ks/ks-index.html 
		//http://www.modelshipgallery.com/gallery/ca/ru/aurora-700-fo/fo-index.html
		//http://www.modelshipgallery.com/gallery/dd/hmas/hobart-350-pc/hobart-index.html
		ModelShipGalleryContentResourceCrawler m = new ModelShipGalleryContentResourceCrawler();
		//String ss = "USS Pittsburg CA-72 by Manuel González";
		//System.out.println(ss.substring(ss.indexOf("by"),ss.length()));
		//i、null http://www.modelshipgallery.com/gallery/ca/ca-139/350-rw/ca-139-rw-index.html
		//m.crawler("http://www.modelshipgallery.com/gallery/dd/hmas/hobart-350-pc/hobart-index.html");
	}
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 modelshipgallery 论坛帖子 来源链接地址：{}",url);
			LinkUrlBean bean  = getContextUrl(url);
			Connection conn = Jsoup.connect(bean.getMainUrl());
			conn.timeout(TIME_OUT*5);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			//System.out.println(doc);
			Elements font = doc.select("font");
			Content content = new Content();
			content.setLikurl(url);
			content.setSitename(SITE_NAME);
			content.setSiteurl(MATCH_URL);
			//translator = new TranslatorService(); //测试时候使用
			//System.out.println(font.get(1).html());
			StringBuilder sb = new StringBuilder();
			String titleWord[] = null;
			String owner = "";
			String titleOld = "";
			if(font.size() == 2){ //特殊情况处理
				String all = StringUtil.regxpForHtml(font.get(0).html(),"");
				if(StringUtil.stringIsEmpty(all)){
					all = StringUtil.regxpForHtml(font.get(1).html(),"");
				}
				if(all.indexOf("by") != -1){
					owner = all.substring(all.indexOf("by"),all.length());
					titleOld = all.substring(0,all.indexOf("by"));
				}else if(all.indexOf("By") != -1){
					owner = all.substring(all.indexOf("By"),all.length());
					titleOld = all.substring(0,all.indexOf("By"));
				}
				titleWord = StringUtil.regxpForHtml(all,"").split(" ");
			}else{
				titleOld = StringUtil.replaceTag(font.get(1).html());
				titleWord = titleOld.split(" ");
				owner = font.get(3).html();
			}
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
			String title = StringUtil.regxpForHtml(StringUtil.replaceTag(sb.toString()),"");
			content.setHeadline(title+" "+titleOld);
			
			//作者
			owner = translator.simpleTranslator(TranslatorType.ENGLISH, owner.toLowerCase().replace("by", ""))+ "|" + owner;
			content.setOwner(StringUtil.replaceTag(owner));
			//图片集合
			List<String> images = getImages(bean.getNavUrl());
			content.setImages(images);
			if(images.size() > 0){
				content.setSimpleImage(images.get(0));
			}
			Elements ps = doc.select("p");
			StringBuilder sbinfo = new StringBuilder();
			for(Element p : ps){
				String strs[] = StringUtil.regxpForHtml(p.html(),",").split(",");
				for(String str:strs){
					if(!StringUtil.stringIsEmpty(str)){
						sbinfo.append(translator.simpleTranslator(TranslatorType.ENGLISH,str));
					}
				}
			}
			//内容
			content.setInfo(StringUtil.replaceTag(title + sbinfo.toString()));
			content.setMultiData(SiteInfo.EUROPE.getCode());
			list.add(content);
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
	private LinkUrlBean getContextUrl(String url) throws Exception{
		LinkUrlBean bean = new LinkUrlBean();
		Connection conn = Jsoup.connect(url);
		conn.timeout(TIME_OUT*5);
		conn.userAgent(USERAGENT);
		Document doc = conn.get(); 
		Elements framemain = doc.select("frame");
		String startUrl = url.substring(0,url.lastIndexOf("/")+1);
		bean.setNavUrl( startUrl + framemain.first().attr("src"));
		bean.setMainUrl( startUrl + framemain.last().attr("src"));
		return bean;
	}
	
	/**
	 * 爬取照片
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private List<String> getImages(String url) throws Exception{
		String startUrl = url.substring(0,url.lastIndexOf("/")+1);
		List<String> list = new ArrayList<String>();
		Connection conn = Jsoup.connect(url);
		conn.timeout(TIME_OUT*5);
		conn.userAgent(USERAGENT);
		Document doc = conn.get(); 
		Elements as = doc.select("a");
		int index = 0;
		for(Element a : as){
			String todo = a.attr("target");
			if("_top".equals(todo)){
				continue;
			}
			String imgurl = a.attr("href");
			if(imgurl.substring(imgurl.lastIndexOf("."),imgurl.length()).contains("htm")){ //是html页面
				try{
					if(imgurl.startsWith("../")){
						continue;
					}
					conn = Jsoup.connect(startUrl + imgurl);
					doc = conn.get(); 
					String img = doc.select("img").first().attr("src");
					if(img.startsWith("../")){
						img = img.substring(3,img.length());
					}
					list.add(startUrl + img);
				} catch (Exception e){
					logger.error("请求地址超时{},url:{}",e,startUrl + imgurl);
				}
			} else { //图片
				list.add(startUrl + imgurl);
			}
			index ++ ;
			if(index > 5){
				break;
			}
			crawlerSheep(500);
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
	
	/**
	 * 本网站使用iframe
	 * @author Administrator
	 *
	 */
	private class LinkUrlBean {
		
		private String navUrl;
		private String mainUrl;
		
		public String getNavUrl() {
			return navUrl;
		}
		public void setNavUrl(String navUrl) {
			this.navUrl = navUrl;
		}
		public String getMainUrl() {
			return mainUrl;
		}
		public void setMainUrl(String mainUrl) {
			this.mainUrl = mainUrl;
		}
		
	}
	
}

package com.mxk.crawler.crawlers.militarymodelling;

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
import com.mxk.crawler.crawlers.modelshipgallery.ModelShipGalleryContentResourceCrawler;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Content;
import com.mxk.crawler.model.Links;
import com.mxk.translator.TranslatorService;
import com.mxk.translator.TranslatorType;
import com.mxk.util.StringUtil;

/**
 * 
 * @author Administrator 
 *  http://www.militarymodelling.com/news/browseModern.asp?at=59&atg=2&p=1
 */
@Service
@CrawlerDescription(crawlerSite= MilitarymodellingContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = MilitarymodellingContentResourceCrawler.MATCH_URL)
public class MilitarymodellingContentResourceCrawler extends Crawler {

	@Value("${system.militarymodelling.crawler.resource.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(ModelShipGalleryContentResourceCrawler.class);
	public static final String MATCH_URL = "http://www.militarymodelling.com/forums/postings.asp";
	public static final String SITE_NAME = "MilitaryModelling";
	public static final String SITE_URL = "http://www.militarymodelling.com";
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 MilitaryModelling 论坛帖子内容 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT*10);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			List<String> images = new ArrayList<String>();
			//专辑图片
			String ablumImage  = doc.select("p[class=AlbumPhotoViewer]").select("img").attr("src");
			int index = 0;
			if(!StringUtil.stringIsEmpty(ablumImage)){
				images.add(SITE_URL + ablumImage);
				index++;
			}
			//所有图片
			Elements imgs = doc.select("table[class=StdText]").select("td[class=ForumPostingBox]").select("img");
			for(Element img : imgs){
                if(index > 5){
					break;
				}
                String imgurl = img.attr("src");
                if(!"gif".equals(StringUtil.getFileSuffixName(imgurl).toLowerCase())){
                	if(imgurl.startsWith("http://")){
                		images.add(imgurl);
                	}else{
                		images.add(SITE_URL + imgurl.substring(imgurl.indexOf("/"),imgurl.length()));
                	}
    				index++;
                }
				
			}
			if(!images.isEmpty()){
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				String title = doc.select("h1[id=PostTop]").html();
				translator = new TranslatorService(); //测试的时候打开
				String translator_title = translator.simpleTranslator(TranslatorType.ENGLISH, title);
				content.setHeadline(StringUtil.replaceTag(translator_title) + "(" + title + ")");
				content.setMultiData(SiteInfo.EUROPE.getCode());
				Elements td =  doc.select("table[class=StdText]").select("tr");
				String owner = td.get(0).select("a").first().html();
				content.setOwner(owner);
				String info = td.get(1).select("td[class=ForumPostingBox]").html();
				info = translator.simpleTranslator(TranslatorType.ENGLISH, stringFormat(info));
				content.setInfo(StringUtil.replaceTag(info));
				content.setImages(images);
				content.setSimpleImage(images.get(0));
				list.add(content);
			}
		}catch(Exception e){
			logger.error("MilitaryModelling crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME * 3);
			logger.info("MilitaryModelling 完成帖子爬取{},爬取link数量：{}",url, list.size());
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
			resource.setHit(str[0]);
			resource.setPost(str[1]);
		}
	}
	
	private String stringFormat(String str){
		Pattern p = Pattern.compile("\n");//去掉换行
	    Matcher m = p.matcher(str);
	    str = m.replaceAll("");//去掉换行
		Pattern patternHtml = Pattern.compile("<([^>]*)>");
    	Matcher matcherHtml = patternHtml.matcher(str);
    	StringBuffer sbHtml = new StringBuffer();
    	boolean result1 = matcherHtml.find();
    	while (result1) { 
    	   matcherHtml.appendReplacement(sbHtml,"");
    	   result1 = matcherHtml.find();
    	}
    	matcherHtml.appendTail(sbHtml);
    	str = sbHtml.toString();//去除html
    	str = str.replaceAll("&nbsp;", "");
    	str = str.replaceAll("&quot;", "");
	    if(str.length() >= 350){
	    	str = str.substring(0,350);
	    }
	    return str;//截取一部分 ;
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

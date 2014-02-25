package com.mxk.crawler.crawlers.arcair;

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
import com.mxk.crawler.crawlers.dishmodels.DishModelsLinkCrawler;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Content;
import com.mxk.crawler.model.Links;
import com.mxk.translator.TranslatorService;
import com.mxk.translator.TranslatorType;
import com.mxk.util.RegxpEnumUtil;
import com.mxk.util.StringUtil;

/**
 * 增量爬取器  爬取Arcair网站最新信息
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite = ArcairModelsSiteContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = ArcairModelsSiteContentResourceCrawler.MATCH_URL)
public class ArcairModelsSiteContentResourceCrawler extends Crawler {

	@Value("${system.arcair.site.crawler.resource.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(DishModelsLinkCrawler.class);
	public static final String MATCH_URL = "http://www.arcair.com/";
	public static final String SITE_URL = "http://www.arcair.com/";
	public static final String SITE_NAME = "Arcair";
	
	public static void main(String[] args) {
		ArcairModelsSiteContentResourceCrawler a = new ArcairModelsSiteContentResourceCrawler();
		Links l = new Links();
		//http://www.arcair.com/
		//
		//l.setUrl("http://www.arcair.com/homepage-archive/2007/indexApr07.htm");
		//l.setUrl("http://www.arcair.com/homepage-archive/2007/indexJuly07.htm");
		l.setUrl("http://www.arcair.com/Rev7/6401-6500/rev6436-Master-AM48-068/00.shtm");
		//a.crawler(l);
//		String ur = a.getSupperUrl("http://www.arcair.com/Gal13/12801-12900/gal12804-LPD-21-Yuen/00.shtm");
//		System.out.println(ur);
//		ur = a.getSupperFloder("http://www.arcair.com/Gal13/12801-12900/gal12804-LPD-21-Yuen/00.shtm");
//		System.out.println(ur);
	}
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Content> list = new ArrayList<Content>();
		String superUrl = getSupperUrl(url);
		try{
			logger.info("开始爬取 Arcair 网站链接资源 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get();  //div dm_bodyblock
			Elements imgs = doc.select("img");
			List<String> images = new ArrayList<String>();
			int index = 0;
			for (Element img : imgs) {
				String imageurl = img.attr("src");
				if(!"gif".equals(StringUtil.getFileSuffixName(imageurl).toLowerCase())){
					images.add( superUrl + imageurl );
					index ++;
				}
				if(index >= 5){
					break;
				}
			}
			if(images.size() > 0){
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				content.setImages(images);
				content.setSimpleImage(images.get(0));
				content.setMultiImageName(getSupperFloder(url)+"_");
				String title = doc.select("title").first().html();//题目
				String info = doc.select("font[color=#004080]").html();
				title = htmlInfoManage(title);
				info = htmlInfoManage(info);
				translator = new TranslatorService(); //测试时候使用
				String titleWord[] = title.split(" ");
				translator = new TranslatorService();
				StringBuilder sb = new StringBuilder();
				for(String str : titleWord){
					if(!StringUtil.stringIsEmpty(str) && str.matches(RegxpEnumUtil.ENGLISH_LOWER_UPPER_CASE.getCode())){ //是否是英语
						String tran = translator.professionalTranslator(str.toLowerCase());
						if(tran != null){
							sb.append(tran + " ");
						}else{
							sb.append(translator.simpleTranslator(TranslatorType.ENGLISH, str) + " ");
						}
					}else{
						sb.append(str + " ");
					}
				}
				content.setHeadline(StringUtil.replaceTag(sb.toString() + "("+ title +")"));
				if(info.length() > 400){ //截取
					info = info.substring(0,300);
				}
				String infoWord[] = info.split(" ");
				StringBuilder sbinfo = new StringBuilder();
				for(String str : infoWord){
					if(!StringUtil.stringIsEmpty(str) && str.matches(RegxpEnumUtil.ENGLISH_LOWER_UPPER_CASE.getCode())){ //是否是英语
						String tran = translator.professionalTranslator(str.toLowerCase());
						if(tran != null){
							sbinfo.append(tran + " ");
						}else{
							sbinfo.append(translator.simpleTranslator(TranslatorType.ENGLISH, str) + " ");
						}
					}else{
						sbinfo.append(str + " ");
					}
				}
				content.setInfo(sbinfo.toString());
				content.setOwner("");
				content.setMultiData(SiteInfo.EUROPE.getCode());
				list.add(content);
			}
		}catch(Exception e){
			logger.error("Arcair crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("Arcair 完成链接爬取{},爬取link数量：{}",url, list.size());
		}
		return list;
	}

	private String htmlInfoManage(String html){
		Pattern p = Pattern.compile("\n");//去掉换行
	    Matcher m = p.matcher(html);
	    String regxp = m.replaceAll("");
	    return StringUtil.regxpForHtml(regxp,"");
	}
	
	private String getSupperUrl(String url){
		return url.substring(0,url.lastIndexOf("/")+1);
	}
	
	private String getSupperFloder(String url){
		String str = url.substring(0,url.lastIndexOf("/"));
		return str.substring(str.lastIndexOf("/")+1,str.length());
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

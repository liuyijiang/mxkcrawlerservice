package com.mxk.crawler.crawlers.dbmodel;

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
import com.mxk.translator.TranslatorService;
import com.mxk.translator.TranslatorType;
import com.mxk.util.RegxpEnumUtil;
import com.mxk.util.StringUtil;

@Service
@CrawlerDescription(crawlerSite = DbmodelContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = DbmodelContentResourceCrawler.MATCH_URL)
public class DbmodelContentResourceCrawler extends Crawler {

	@Value("${system.dbmodel.crawler.resource.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(DbmodelLinkCrawler.class);
	public static final String MATCH_URL = "http://bbs.dbmodel.com/viewthread.php";
	public static final String SITE_NAME = "东北模型论坛";
	public static final String SITE_URL = "http://bbs.dbmodel.com/";
	
	private static final String NEED_TRANSLATED = "http://bbs.dbmodel.com/forumdisplay.php?fid=5&page";
	private static final String REGXP = " <div class=\"cornerlayger\">(.*)</div>"; 
	private static final String REGXP2 = "<div (.*) class=\"t_attach\" (.*)>(.*)</div>";
	
	public static void main(String[] args) {
		DbmodelContentResourceCrawler d = new DbmodelContentResourceCrawler();
		Links l = new Links();
		l.setUrl("http://bbs.dbmodel.com/viewthread.php?tid=8250&extra=page%3D26");
		l.setFromUrl("http://bbs.dbmodel.com/forumdisplay.php?fid=5&page");
		d.crawler(l);
//		String str = "[转帖]Pro Built! Dragon 1:35 WWII German Panzer III";
//		System.out.println(str.replaceAll("\\[转帖\\]", ""));
	}
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 东北模型论坛帖子内容 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent(USERAGENT);
			Document doc = conn.get(); 
			//image 
			Elements image = doc.select("div[class=postmessage firstpost]").first().select("img");
			if(image != null && image.size() > 0) {
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				List<String> images = new ArrayList<String>();
				int index = 0;
				for(Element img : image){
					if(!StringUtil.stringIsEmpty(img.attr("file"))){
						images.add(SITE_URL + img.attr("file"));
						index++;
					}else if(!StringUtil.stringIsEmpty(img.attr("src")) && !"gif".equals(StringUtil.getFileSuffixName(img.attr("src")).toLowerCase())){
						// && !StringUtil.getFileSuffixName(img.attr("src")).contains("")
						images.add(img.attr("src"));	
						index++;
					}
					if(index >= 5){
						break;
					}
				}
				content.setImages(images);
				content.setSimpleImage(images.get(0));
				//处理标题
				String title = doc.select("div[id=threadtitle]").first().select("h1").html();
				boolean needRebuild = false;
				if(title.contains("[转帖]")){
					needRebuild = true;
				}
				String translator_title = title.replaceAll("\\[转帖\\]", "");
				//需要翻译
				if(!StringUtil.isChinese(translator_title) && flink.getFromUrl().startsWith(NEED_TRANSLATED)){
					translator = new TranslatorService(); //测试时候使用
					String titleWord[] = translator_title.split(" ");
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
					if(needRebuild){
						translator_title = "[转帖]" + sb.toString();
					}else{
						translator_title = sb.toString();
					}
					content.setInfo(translator_title + title);
					content.setHeadline(translator_title);
				}else{ //不需要翻译
					String info = doc.select("div[class=postmessage firstpost]").first().html();
					content.setInfo(title+htmlInfoManage(info).replaceAll(" ", ""));
					content.setHeadline(title);
				}
				String owner = doc.select("div[class=postinfo]").first().select("a").html();
				content.setOwner(owner);
				content.setMultiData(SiteInfo.CHINIA.getCode());
				list.add(content);
			}
		}catch(Exception e){
			logger.error("东北模型论坛 crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("东北模型论坛 完成帖子爬取{},爬取link数量：{}",url, list.size());
		}
		return list;
	}
	
	private String htmlInfoManage(String html){
		Pattern p = Pattern.compile("\n");//去掉换行
	    Matcher m = p.matcher(html);
	    String regxp = m.replaceAll("").replaceAll(REGXP, "");
	    regxp = regxp.replaceAll(REGXP2, "");
	    return StringUtil.regxpForHtml(regxp,"");
	}
	
	@Override
    public void packageData(Links link,Content resource){
		if(link.getMultiData() != null){
			String[] str = link.getMultiData().split(",");
			resource.setHit(str[0]);
			resource.setPost(str[1]);
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

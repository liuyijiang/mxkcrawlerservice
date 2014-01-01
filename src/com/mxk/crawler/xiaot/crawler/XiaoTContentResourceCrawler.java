package com.mxk.crawler.xiaot.crawler;

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
 * 爬取  xiaot模型空间站 谓论坛链接资源 http://www.xiaot.com
 * @author Administrator 帖子
 *
 */
@Service
@CrawlerDescription(crawlerSite=XiaoTContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = XiaoTContentResourceCrawler.MATCH_URL)
public class XiaoTContentResourceCrawler extends Crawler {

	@Value("${system.xiaot.crawler.resource.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(XiaoTContentResourceCrawler.class);
	
	public static final String MATCH_URL = "http://bbs.xiaot.com/forum.php?mod=viewthread";
	public static final String SITE_URL = " http://www.xiaot.com/";
	public static final String SITE_NAME = "xiaot模型空间站";
	
	public static void main(String[] args) {
		XiaoTContentResourceCrawler x =new XiaoTContentResourceCrawler();
		x.crawler("http://bbs.xiaot.com/forum.php?mod=viewthread&tid=936932&extra=page%3D1%26filter%3Dtypeid%26typeid%3D115%26typeid%3D115");
	}
	
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 "+ SITE_NAME +"论坛 帖子链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)"); //Jsoup中默认没有指定User-Agent，网站的服务器则认为这个访问来自手机，返回的是手机的页面
			Document doc = conn.get(); 
			Elements imgs = doc.select("img[class=zoom]"); //span id="thread_subject" a[class=zoom]
			if(!imgs.isEmpty()){
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				int index = 1;
				List<String> images = new ArrayList<String>();
				content.setImages(images);
				for(Element img : imgs){
					if(index <= CRAWLER_IMAGE_COUNT){
						if(!StringUtil.stringIsEmpty(img.attr("file"))){
							images.add(SITE_URL + img.attr("file"));
						}else{
							images.add(img.attr("src"));	
						}
						index ++;
					}else{
						break;
					}
				}
				content.setOwner(doc.select("a[class=xw1]").first().html());//作者
				content.setHeadline(doc.select("span[id=thread_subject]").html());
				content.setPost(doc.select("div[class=hm ptn]").select("span[class=xi1]").last().html());
				
				Elements comments = doc.select("td[class=t_f]");
				StringBuffer sb = new StringBuffer();
				for(int i=1;i<comments.size();i++){
					sb.append(comments.get(i).html());
				}
				content.setComment(StringUtil.regxpForSymbol(StringUtil.regxpForHtml(sb.toString())));
				list.add(content);
			}
		}catch(Exception e){
			logger.error(SITE_NAME +" crawler error url: {} message :{}",url,e.getMessage());
			crawlerSheep(SHEEP_TIME * 3);
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info(SITE_NAME+" 完成链接爬取{},爬取link数量：{}",url, list.size());
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

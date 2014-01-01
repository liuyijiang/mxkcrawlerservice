package com.mxk.crawler.modelship.crawler;

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
 * 爬取钢铁浮城论坛帖子资源 http://forum.modelship.com.tw  
 * @author Administrator  http://forum.modelship.com.tw/phpBB2/viewforum.php?f=3&topicdays=0&start=50
 * 初始link http://forum.modelship.com.tw/phpBB2/viewforum.php?f=3&topicdays=0&start=0
 */
@Service
@CrawlerDescription(crawlerSite=ModelShipContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = ModelShipContentResourceCrawler.MATCH_URL)
public class ModelShipContentResourceCrawler  extends Crawler {

	@Value("${system.modelship.crawler.resource.execute}")
	private boolean execute;
	
    public static final Logger logger = LoggerFactory.getLogger(ModelShipContentResourceCrawler.class);
	
	public static final String MATCH_URL = "http://forum.modelship.com.tw/phpBB2/viewtopic.php";
	public static final String SITE_URL = "http://forum.modelship.com.tw/phpBB2/";
	public static final String SITE_NAME = "钢铁浮城";
	
	public static void main(String[] args) {
		//http://forum.modelship.com.tw/phpBB2/viewtopic.php?t=3360
		ModelShipContentResourceCrawler m = new ModelShipContentResourceCrawler();
		m.crawler("http://forum.modelship.com.tw/phpBB2/viewtopic.php?t=210");
	}
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取钢铁浮城 论坛帖子内容 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			Document doc = conn.get(); 
			Elements imgs =  doc.select("span[class=postbody]").first().select("img");
			int index = 1; // 计数器
			if(!imgs.isEmpty()){
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				List<String> imagelist = new ArrayList<String>();
				content.setImages(imagelist);
				for(Element img : imgs){
					if(index <= CRAWLER_IMAGE_COUNT){
						String imgurl = img.attr("src");
						if(!StringUtil.stringIsEmpty(imgurl) && !imgurl.contains(".gif")){
							imagelist.add(imgurl);
							index ++;
						}
					}else{
						break;
					}
				}
				content.setHeadline(doc.select("a[class=maintitle]").html());//标题
				content.setOwner( doc.select("span[class=name]").first().select("b").html());//作者
				Elements comms =  doc.select("span[class=postbody]");
				int post = comms.size() - 1;
				content.setPost(String.valueOf(post >=0?post:0)); //评论数量
				StringBuffer sb = new StringBuffer();
				for(int i=1;i<comms.size();i++){
					sb.append(comms.get(i).html());
				}
				content.setComment(StringUtil.regxpForSymbol(StringUtil.regxpForHtml(sb.toString())));
				list.add(content);
			}
		}catch(Exception e){
			logger.error("钢铁浮城 crawler error url: {} message :{}",url,e.getMessage());
			crawlerSheep(SHEEP_TIME * 3);
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("钢铁浮城 完成链接爬取{},爬取link数量：{}",url, list.size());
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

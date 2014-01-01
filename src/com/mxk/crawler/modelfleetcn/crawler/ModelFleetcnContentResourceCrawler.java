package com.mxk.crawler.modelfleetcn.crawler;

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
 * 保存爬取海权无谓论坛上的帖子资源
 * @author Administrator
 *
 */
@Service
@CrawlerDescription(crawlerSite=ModelFleetcnContentResourceCrawler.SITE_NAME , crawlerType=CrawleType.CONTENT, crawlerMatchUrl = ModelFleetcnContentResourceCrawler.MATCH_URL)
public class ModelFleetcnContentResourceCrawler extends Crawler {

    public static final Logger logger = LoggerFactory.getLogger(ModelFleetcnContentResourceCrawler.class);
    
	public static final String MATCH_URL = "http://bbs.modelfleetcn.com/forum.php?mod=viewthread"; //帖子的链接
	public static final String SITE_URL = "http://bbs.modelfleetcn.com/";
	public static final String SITE_NAME = "海权无谓";
	public static final String SITE_IMAGE = "http://bbs.modelfleetcn.com/"; //"http://farm4.staticflickr.com
	
	@Value("${system.modelfleetcn.crawler.resource.execute}")
	private boolean execute;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelFleetcnContentResourceCrawler m = new ModelFleetcnContentResourceCrawler();
		m.crawler("http://bbs.modelfleetcn.com/forum.php?mod=viewthread&tid=531&extra=page%3D4"); //http://bbs.modelfleetcn.com/forum.php?mod=viewthread&tid=833&extra=page%3D1

	}
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取海权无谓 论坛帖子内容 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			Document doc = conn.get(); 
			Elements images = doc.select("img[class=zoom]"); //
			if(images == null || images.size() == 0){ // 特殊情况
				images = doc.select("td[class=t_f]").select("img");//<td id="postmessage_3648" class="t_f">
			}
			int index = 1; // 计数器
			if(images != null && images.size() > 0){
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				List<String> imagelist = new ArrayList<String>();
				content.setImages(imagelist);
				for (Element img : images) {
					if(index <= CRAWLER_IMAGE_COUNT){
						if(!StringUtil.stringIsEmpty(img.attr("file"))){
							imagelist.add(SITE_IMAGE + img.attr("file"));
						}else{
							imagelist.add(img.attr("src"));	
						}
						index ++;
					}else{
						break;
					}
				}
				content.setPost(doc.select("span[class=xi1]").last().html()); //评论数量
				content.setHeadline(doc.select("a[id=thread_subject]").html()); //题目
				content.setOwner(doc.select("a[class=xw1]").first().html());
				StringBuffer sb = new StringBuffer();
				Elements comms = doc.select("td[class=t_f]");
				for(int i=1;i<comms.size();i++){
					sb.append(comms.get(i).html());
				}
				content.setComment(StringUtil.regxpForSymbol(StringUtil.regxpForHtml(sb.toString())));
				list.add(content);
			}
		}catch(Exception e){
			logger.error("海权无谓  crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("海权无谓  完成链接爬取{},爬取link数量：{}",url, list.size());
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

package com.mxk.crawler.crawlers.xiaot;

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
import com.mxk.crawler.model.ResourceState;
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
	
	private static final String REGXP = "<ignore_js_op>(.*)</ignore_js_op>"; 
	
	public static void main(String[] args) {
		XiaoTContentResourceCrawler x =new XiaoTContentResourceCrawler();
		x.crawler("http://bbs.xiaot.com/forum.php?mod=viewthread&tid=906933&extra=page%3D2%26filter%3Dtypeid%26typeid%3D116%26typeid%3D116");
	}
	
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Content> list = new ArrayList<Content>();
		try{
			logger.info("开始爬取 "+ SITE_NAME +"论坛 帖子链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			conn.header("Cookie", "pgv_pvi=6591002264; Hm_lvt_d9cf7845f5453ac918316913a7a7a3af=1388134356,1388368178,1389854187,1390220963; zw7z_994d_saltkey=a9IQTqtc; zw7z_994d_lastvisit=1387942956; zw7z_994d_forum_lastvisit=D_92_1390221702; zw7z_994d_sid=rlNzR4; zw7z_994d_lastact=1390221702%09forum.php%09forumdisplay; pgv_info=ssi=s676067994&ssid=s1940719700; Hm_lpvt_d9cf7845f5453ac918316913a7a7a3af=1390221666; zw7z_994d_hideQrcode=1; PHPSESSID=j33dso4idh1s97vcgv2nbdj9d3; zw7z_994d_viewid=tid_937690; pgv_pvid=5476179248");
			conn.header("Host", "bbs.xiaot.com");
			conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
			conn.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)"); //Jsoup中默认没有指定User-Agent，网站的服务器则认为这个访问来自手机，返回的是手机的页面
			Document doc = conn.get(); 
			Elements imgs = doc.select("ignore_js_op").select("img[class=zoom]"); //span id="thread_subject" a[class=zoom]
			if(!imgs.isEmpty()){
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				List<String> images = new ArrayList<String>();
				int index = 0;
				for(Element img : imgs){
					index++;
					if(!StringUtil.stringIsEmpty(img.attr("file"))){
						images.add(SITE_URL + img.attr("file"));
					}else{
						images.add(img.attr("src"));	
					}
					if(index >= 5){
						break;
					}
				}
				content.setImages(images);
				content.setSimpleImage(images.get(0));
				content.setOwner(doc.select("a[class=xw1]").first().html());//作者
				content.setHeadline(doc.select("span[id=thread_subject]").html());
				content.setHit(doc.select("div[class=hm ptn]").select("span[class=xi1]").first().html());
				content.setPost(doc.select("div[class=hm ptn]").select("span[class=xi1]").last().html());
				content.setMultiData(SiteInfo.CHINIA.getCode());
			    String info = doc.select("td[class=t_f]").html();
                content.setInfo(htmlInfoManage(info));
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
	
	/**
	 * 处理爬取下来的内容
	 * @param html
	 * @return
	 */
	private String htmlInfoManage(String html){
		Pattern p = Pattern.compile("\n");//去掉换行
	    Matcher m = p.matcher(html);
	    return StringUtil.regxpForHtml(m.replaceAll("").replaceAll(REGXP, ""),"");
	}
	
	@Override
	public boolean checkExecute() {
		return execute;
	}
	
	@Override
	public void setExecute(boolean runable) {
		super.setExecute(runable);
		this.execute = runable;
	}
	
}

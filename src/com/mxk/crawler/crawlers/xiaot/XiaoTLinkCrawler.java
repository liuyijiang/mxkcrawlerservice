package com.mxk.crawler.crawlers.xiaot;

import java.util.ArrayList;
import java.util.Date;
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
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;
import com.mxk.util.RegxpEnumUtil;

/**
 * 爬取 xiaot模型空间站 谓论坛链接资源 http://www.xiaot.com
 * @author Administrator  bbs.xiaot.com/forum.php?mod=forumdisplay
 *http://bbs.xiaot.com/forum.php?mod=forumdisplay&fid=92&filter=typeid&typeid=115  forum.php?mod=viewthread&amp;tid=936932& forum.php?mod=viewthread&amp;
 */
@Service
@CrawlerDescription(crawlerSite = XiaoTLinkCrawler.SITE_NAME , crawlerType=CrawleType.LINK, crawlerMatchUrl = XiaoTLinkCrawler.MATCH_LINK_URL)
public class XiaoTLinkCrawler extends Crawler {

	@Value("${system.xiaot.crawler.link.execute}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(XiaoTLinkCrawler.class);
	public static final String MATCH_LINK_URL = "http://bbs.xiaot.com/forum.php?mod=forumdisplay";
	public static final String MATCH_CONTENT_URL = "http://bbs.xiaot.com/forum.php?mod=viewthread";
	public static final String SITE_URL = " http://www.xiaot.com/";
	public static final String SITE_NAME = "xiaot模型空间站";
  
	public static final String XIAO_T_MATCH_LINK_URL = "forum.php?mod=forumdisplay";
	public static final String XIAO_TMATCH_CONTENT_URL = "forum.php?mod=viewthread";
	
	@Override
	public List<? extends BaseResource> crawler(String url) {
		List<Links> list = new ArrayList<Links>();
		try{
			logger.info("开始爬取 xiaot模型空间站 论坛链接 来源链接地址：{}",url);
			Connection conn = Jsoup.connect(url);
			conn.timeout(TIME_OUT);
			//由cookie来确定走浏览器还是手机
     		conn.header("Cookie", "	pgv_pvi=6591002264; Hm_lvt_d9cf7845f5453ac918316913a7a7a3af=1388134356,1388368178,1389854187,1390220963; zw7z_994d_saltkey=a9IQTqtc; zw7z_994d_lastvisit=1387942956; zw7z_994d_forum_lastvisit=D_92_1390221702; zw7z_994d_sid=rlNzR4; zw7z_994d_lastact=1390221702%09forum.php%09forumdisplay; pgv_info=ssi=s676067994&ssid=s1940719700; Hm_lpvt_d9cf7845f5453ac918316913a7a7a3af=1390221666; zw7z_994d_hideQrcode=1; PHPSESSID=j33dso4idh1s97vcgv2nbdj9d3; zw7z_994d_viewid=tid_937690; pgv_pvid=5476179248");
			conn.header("Host", "bbs.xiaot.com");
			conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
			Document doc = conn.get();
			Elements ths = doc.select("th[class=common]"); //Elements as = doc.select("div[class=threadlist]");<th class="common">s xst th[class=common]
			
			Elements pga = doc.select("div[class=pg]").first().select("a");
			for(Element pa : pga){
				Links link = new Links();
				link.setFromUrl(url);
				link.setCreateTime(new Date());
				link.setUrl(pa.attr("href"));
				link.setState(ResourceState.NO_CRAWLER.getCode());
				link.setMatchUrl(MATCH_LINK_URL);
				list.add(link);
				logger.info("爬取到xiaot模型空间站 帖子链接：{}", link.getUrl());
			}
			for(Element th : ths){
				Element a = th.select("a").last();
				if(!a.html().matches(RegxpEnumUtil.NUMBER.getCode())){
					String links = a.attr("href");
					Links link = new Links();
					link.setFromUrl(url);
					link.setCreateTime(new Date());
					link.setUrl(links);
					link.setState(ResourceState.NO_CRAWLER.getCode());
					link.setMatchUrl(MATCH_CONTENT_URL);
					list.add(link);
					logger.info("爬取到xiaot模型空间站 帖子链接：{}", link.getUrl());
				}
			}
		}catch(Exception e){
			logger.error("crawler error url: {} message :{}",url,e.getMessage());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("xiaot模型空间站 完成链接爬取{},爬取link数量：{}",url, list.size());
		}
		return list;	
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

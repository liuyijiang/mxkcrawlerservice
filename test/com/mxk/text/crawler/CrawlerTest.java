package com.mxk.text.crawler;

import org.junit.Ignore;
import org.junit.Test;

import com.mxk.crawler.crawlers.militarymodelling.MilitarymodellingContentResourceCrawler;
import com.mxk.crawler.crawlers.militarymodelling.MilitarymodellingLinkCrawler;
import com.mxk.crawler.crawlers.xiaot.XiaoTLinkCrawler;
/**
 * 爬取器测试
 * @author Administrator
 *
 */
public class CrawlerTest {

	@Test
	@Ignore
	public void testMilitarymodellingLinkCrawler(){
		MilitarymodellingLinkCrawler m = new MilitarymodellingLinkCrawler();
		m.crawler("http://www.militarymodelling.com/news/browseModern.asp?at=59&atg=2&p=1");
	}
	
	@Test
	@Ignore
	public void testMilitarymodellingContentResourceCrawler(){
//		String str = "II";
//		System.out.println(str.matches("^((l|L)?(x|X){0,3}|(x|X)[lc]|[LC])((v|V)?(i|I){0,3}|(i|I)[vx]|[VX])$"));
		MilitarymodellingContentResourceCrawler m = new MilitarymodellingContentResourceCrawler();
		m.crawler("http://www.militarymodelling.com/news/article/busy-bob%27s-1-6-pz-ii-ausf-b/15506");
	}

	//xiaot
	@Test
	public void testXiaoTLinkCrawler(){
//		String str = "dd8";
//		System.out.println(str.matches("^[0-9]*$"));
		
		
		XiaoTLinkCrawler x = new XiaoTLinkCrawler();
		x.crawler("http://bbs.xiaot.com/forum.php?mod=forumdisplay&fid=92&filter=typeid&typeid=115");
		
	}
	
	
}

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
		//m.crawler("http://www.militarymodelling.com/news/browseModern.asp?at=59&atg=2&p=1");
	}
	
	@Test
	@Ignore
	public void testMilitarymodellingContentResourceCrawler(){
//		String str = "II";
//		System.out.println(str.matches("^((l|L)?(x|X){0,3}|(x|X)[lc]|[LC])((v|V)?(i|I){0,3}|(i|I)[vx]|[VX])$"));
		MilitarymodellingContentResourceCrawler m = new MilitarymodellingContentResourceCrawler();
		//m.crawler("http://www.militarymodelling.com/news/article/busy-bob%27s-1-6-pz-ii-ausf-b/15506");
	}

	//xiaot
	@Test
	public void testXiaoTLinkCrawler(){
//		String str = "dd8";
//		System.out.println(str.matches("^[0-9]*$"));
		
		
//		XiaoTLinkCrawler x = new XiaoTLinkCrawler();
//		x.crawler("http://bbs.xiaot.com/forum.php?mod=forumdisplay&fid=92&filter=typeid&typeid=115");
		
	}
	
	@Test
	public void testString(){
		String regex = "<div class=\"t_attach\" ([^>]*)>(.*)</div>";
		String str = "<div class='t_attach' id='aimg_14263_menu' style='position: absolute; display: none'><a href='attachment.php?aid=MTQyNjN8ZDE4ZjliYWR8MTM5MTUxODc1MXw0MjExYVd1ZTZWTmVDVE0zN2x0bDd3WkhqSXczUGo5SUdYYUZVMkpId2lQbVhxcw%3D%3D&amp;nothumb=yes' title='_MG_4503副本.jpg' target='_blank'><strong>下载</strong></a> (318.33 KB)<br /><div class='t_smallfont'><span title='2014-2-3 22:54'>昨天&nbsp;22:54</span></div></div>dsad";
	    System.out.println(str.replaceAll(regex, ""));
	
	}
	
}

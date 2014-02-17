package com.mxk.text.crawler;

import org.junit.Ignore;
import org.junit.Test;

import com.mxk.crawler.crawlers.militarymodelling.MilitarymodellingContentResourceCrawler;
import com.mxk.crawler.crawlers.militarymodelling.MilitarymodellingLinkCrawler;
import com.mxk.crawler.crawlers.xiaot.XiaoTLinkCrawler;
import com.mxk.crawler.model.Links;
/**
 * 爬取器测试
 * @author Administrator
 *
 */
public class CrawlerTest {

	
	@Test
	public void testMilitarymodellingLinkCrawler(){
		MilitarymodellingLinkCrawler m = new MilitarymodellingLinkCrawler();
		Links link = new Links();
		link.setUrl("http://www.militarymodelling.com/forums/threads.asp?t=97");
		m.crawler(link);
		//m.crawler("http://www.militarymodelling.com/news/browseModern.asp?at=59&atg=2&p=1");
	}
	
	@Test
	public void testMilitarymodellingContentResourceCrawler(){
		/**
		 * error url 
		 * http://www.militarymodelling.com/forums/postings.asp?th=77989
		 * http://www.militarymodelling.com/forums/postings.asp?th=76413
		 * http://www.militarymodelling.com/forums/postings.asp?th=77895
		 */
		MilitarymodellingContentResourceCrawler m = new MilitarymodellingContentResourceCrawler();
		Links link = new Links();
		link.setUrl("http://www.militarymodelling.com/forums/postings.asp?th=79557");
		m.crawler(link);
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

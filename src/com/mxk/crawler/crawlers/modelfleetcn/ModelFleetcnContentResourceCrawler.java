package com.mxk.crawler.crawlers.modelfleetcn;

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
import com.mxk.crawler.model.ResourceState;
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
	
	private static final String REGXP = "<ignore_js_op>(.*)</ignore_js_op>"; 
	
	@Value("${system.modelfleetcn.crawler.resource.execute}")
	private boolean execute;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelFleetcnContentResourceCrawler m = new ModelFleetcnContentResourceCrawler();
		//m.crawler("http://bbs.modelfleetcn.com/forum.php?mod=viewthread&tid=587&extra=page%3D10"); //http://bbs.modelfleetcn.com/forum.php?mod=viewthread&tid=833&extra=page%3D1
        //String str = "<ignore_js_op>  <img id='aimg_3887' src='static/image/common/none.gif' zoomfile='data/attachment/forum/201301/21/174626z0pv55gxfclhvu56.jpg' file='data/attachment/forum/201301/21/174626z0pv55gxfclhvu56.jpg' class='zoom' onclick='zoom(this, this.src)' width='600' inpost='1' alt='zhiyuan03.jpg' title='zhiyuan03.jpg' onmouseover='showMenu({'ctrlid':this.id,'pos':'12'})' />  <div class='tip tip_4 aimg_tip' id='aimg_3887_menu' style='position: absolute; display: none'>   <div class='tip_c xs0'>    <div class='y'>    2013-1-21 17:46:26 上传   </div>    <a href='forum.php?mod=attachment&amp;aid=Mzg4N3xkMDZkNDlmMnwxMzkwMzU0MTQ5fDB8NTg3&amp;nothumb=yes' title='zhiyuan03.jpg 下载次数:0' target='_blank'><strong>下载附件</strong> <span class='xs0'>(1.01 MB)</span></a>   </div>   <div class='tip_horn'></div>  </div> </ignore_js_op> <br /> <ignore_js_op>  <img id='aimg_3886' src='static/image/common/none.gif' zoomfile='data/attachment/forum/201301/21/174623vzvg8vb2vdomezg2.jpg' file='data/attachment/forum/201301/21/174623vzvg8vb2vdomezg2.jpg' class='zoom' onclick='zoom(this, this.src)' width='600' inpost='1' alt='zhiyuan02.jpg' title='zhiyuan02.jpg' onmouseover='showMenu({'ctrlid':this.id,'pos':'12'})' />  <div class='tip tip_4 aimg_tip' id='aimg_3886_menu' style='position: absolute; display: none'>   <div class='tip_c xs0'>    <div class='y'>    2013-1-21 17:46:23 上传   </div>    <a href='forum.php?mod=attachment&amp;aid=Mzg4Nnw1ZWFmZjliMXwxMzkwMzU0MTQ5fDB8NTg3&amp;nothumb=yes' title='zhiyuan02.jpg 下载次数:0' target='_blank'><strong>下载附件</strong> <span class='xs0'>(1.94 MB)</span></a>   </div>   <div class='tip_horn'></div>  </div> </ignore_js_op> <br /> <br /> <br /> <ignore_js_op>  <img id='aimg_3885' src='static/image/common/none.gif' zoomfile='data/attachment/forum/201301/21/174615nmzt9hvz99krvokh.jpg' file='data/attachment/forum/201301/21/174615nmzt9hvz99krvokh.jpg' class='zoom' onclick='zoom(this, this.src)' width='600' inpost='1' alt='zhiyuan01.jpg' title='zhiyuan01.jpg' onmouseover='showMenu({'ctrlid':this.id,'pos':'12'})' />  <div class='tip tip_4 aimg_tip' id='aimg_3885_menu' style='position: absolute; display: none'>   <div class='tip_c xs0'>    <div class='y'>    2013-1-21 17:46:15 上传   </div>    <a href='forum.php?mod=attachment&amp;aid=Mzg4NXwxOWVhZTczY3wxMzkwMzU0MTQ5fDB8NTg3&amp;nothumb=yes' title='zhiyuan01.jpg 下载次数:0' target='_blank'><strong>下载附件</strong> <span class='xs0'>(1.4 MB)</span></a>   </div>   <div class='tip_horn'></div>  </div> </ignore_js_op> <br /> <br />精彩！....不过这个适合做成水线啊....带低的做战损好少见好妖艳的颜色<img src='static/image/smiley/coolmonkey/05.gif' smilieid='29' border='0' alt='' /><div class='quote'> <blockquote>  <font size='2'></font>  <font color='#999999'>ggyti 发表于 2013-1-22 11:14</font>   <a href='http://bbs.modelfleetcn.com/forum.php?mod=redirect&amp;goto=findpost&amp;pid=2152&amp;ptid=587' target='_blank'><img src='static/image/common/back.gif' onload='thumbImg(this)' alt='' /></a>  <br /> 好妖艳的颜色 </blockquote></div><br /> 妖艳活闪 奔向新时代樓主造這情景還是水線加海面較為合適...還有, 白色的水兵服??没有水面确实缺点场景的带入感加个水景就更好了田的1/350兵人果然只是比PE厚一点的饼人....好在没买.......个人觉得，战损是做模型的最高境界~这个战损应该配个情景才贴切Amazing work of art !!!<br /> <br /> <ignore_js_op>  <img id='aimg_3980' src='static/image/common/none.gif' zoomfile='data/attachment/forum/201301/25/000144mr5ddig9ymdhcsz8.jpg' file='data/attachment/forum/201301/25/000144mr5ddig9ymdhcsz8.jpg' class='zoom' onclick='zoom(this, this.src)' width='335' inpost='1' alt='5star.jpg' title='5star.jpg' onmouseover='showMenu({'ctrlid':this.id,'pos':'12'})' />  <div class='tip tip_4 aimg_tip' id='aimg_3980_menu' style='position: absolute; display: none'>   <div class='tip_c xs0'>    <div class='y'>    2013-1-25 00:01:44 上传   </div>    <a href='forum.php?mod=attachment&amp;aid=Mzk4MHxlZGRlNGU3MnwxMzkwMzU0MTQ5fDB8NTg3&amp;nothumb=yes' title='5star.jpg 下载次数:0' target='_blank'><strong>下载附件</strong> <span class='xs0'>(6.85 KB)</span></a>   </div>   <div class='tip_horn'></div>  </div> </ignore_js_op> <br />";
	   // System.out.println(str.replaceAll(REGXP, ""));
	}
	
	@Override
	public List<? extends BaseResource> crawler(Links flink) {
		String url = flink.getUrl();
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
			int index = 0; // 计数器
			if(images != null && images.size() > 0){
				Content content = new Content();
				content.setLikurl(url);
				content.setSitename(SITE_NAME);
				content.setSiteurl(SITE_URL);
				List<String> imagelist = new ArrayList<String>();
				content.setImages(imagelist);
				for (Element img : images) {
					index++;
					if(!StringUtil.stringIsEmpty(img.attr("file"))){
						imagelist.add(SITE_IMAGE + img.attr("file"));
					}else{
						imagelist.add(img.attr("src"));	
					}
					if(index >= 5){
						break;
					}
				}
				content.setMultiData(SiteInfo.CHINIA.getCode());
				content.setSimpleImage(imagelist.get(0));
				content.setPost(doc.select("span[class=xi1]").last().html()); //评论数量
				content.setHit(doc.select("span[class=xi1]").first().html());
				content.setHeadline(doc.select("a[id=thread_subject]").html()); //题目
				content.setOwner(doc.select("a[class=xw1]").first().html());
				StringBuilder sb = new StringBuilder();
				Elements comms = doc.select("td[class=t_f]");
				for(int i=1;i<comms.size();i++){
					sb.append(comms.get(i).html());
				}
				Pattern p = Pattern.compile("\n");//去掉换行
	    	    Matcher m = p.matcher(sb.toString());
	    	    String info = m.replaceAll("");
	    	    //System.out.println(info);
	    	    content.setInfo(StringUtil.regxpForHtml(info.replaceAll(REGXP, ""),""));
				list.add(content);
			}
		}catch(Exception e){
			logger.error("海权无谓  crawler error url: {} message :{}",url,e.getMessage());
			crawlerService.updateLinkState(url, ResourceState.LINK_READ_TIMED_OUT.getCode());
		}finally{
			crawlerSheep(SHEEP_TIME);
			logger.info("海权无谓  完成链接爬取{},爬取link数量：{}",url, list.size());
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
		this.execute = runable;
	}
}

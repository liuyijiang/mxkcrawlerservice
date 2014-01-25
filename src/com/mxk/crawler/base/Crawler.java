package com.mxk.crawler.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mxk.crawler.BaseFileUploadService;
import com.mxk.crawler.CrawlerService;
import com.mxk.crawler.GridFSFileUploadService;
import com.mxk.crawler.annotation.CrawleType;
import com.mxk.crawler.annotation.CrawlerDescription;
import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.Content;
import com.mxk.crawler.model.ContentResource;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;
import com.mxk.translator.TranslatorService;
import com.mxk.util.HttpUtil;
import com.mxk.util.StringUtil;
import com.mxk.util.ThreadGroupUtil;

/**
 * 抽象的爬取器
 * @author Administrator
 *
 */
public abstract class Crawler {

	@Autowired
	public CrawlerService crawlerService;
	
	@Autowired
	private GridFSFileUploadService gridFSFileUploadService;
	
	@Resource
	private BaseFileUploadService baseFileUploadService;
	
	@Resource
	public TranslatorService translator;
	
	private CrawlerState state;
	
	/** 从注解中获得爬取的网站名字  */
	private String crawlerSite;
	/** 从注解中获得爬取的数据类型 */
	private String crawlerType;
	/** 从注解中获得爬取的网站匹配的url */
	private String crawlerMatchUrl;
	
	
//	/**是否执行爬取 */
//	public boolean execute;
	
	public static final int SHEEP_TIME = 1000;
	
	public static final int TIME_OUT = 5000;
	
	public static final int CRAWLER_IMAGE_COUNT = 5;
	
	/** 模拟浏览器 */
	public static final String USERAGENT = "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
	
	/** 使用线程组开控制线程状态 */
	public final ThreadGroupUtil threadGroup = new ThreadGroupUtil(this.getClass().getName(),true);//每个子类的 是否是守护线程
	
	public static final Logger logger = LoggerFactory.getLogger(Crawler.class);
	
	/**
	 * 抽象出爬取功能 每个子爬取器自己实现
	 * @param url
	 * @return
	 */
	public abstract List<? extends BaseResource> crawler(String url);
	
	/**
	 * 抽象出资源保存功能
	 * @param list
	 * @return
	 */
	public boolean saveResource(List<? extends BaseResource> list){
		CrawlerDescription crawlerDescription = this.getClass().getAnnotation(CrawlerDescription.class);
		CrawleType crawlerType = crawlerDescription.crawlerType();//
		switch (crawlerType) {
		    case LINK : 
		    	return crawlerService.saveLink(list);
		    case CONTENT : 
		    	List<ContentResource> rlist = new ArrayList<ContentResource>();
				for(BaseResource base : list){
					Content content = (Content) base ;
					if(!crawlerService.checkResourceExist(content.getLikurl())){ //判断是否存在资源
						continue;
					}
					ContentResource resource = new ContentResource();
					resource.setComment(content.getComment());
					resource.setHeadline(content.getHeadline());
					resource.setLinkurl(content.getLikurl());
					resource.setOwner(content.getOwner());
					resource.setSitename(content.getSitename());
					resource.setSiteurl(content.getSiteurl());
					resource.setPost(content.getPost());
					resource.setMultiData(content.getMultiData());
					resource.setHit(content.getHit());
					resource.setInfo(content.getInfo());
					resource.setState(ResourceState.NO_CATALOGO.getCode());//未编目
//					List<String> urls = new ArrayList<String>();
//					resource.setImages(urls);
//					for(String image : content.getImages()){
//						byte[] byteFile = HttpUtil.getImageByte(image);
//						String fileName = StringUtil.cutOutUrlFileName(image);
//						if(gridFSFileUploadService.uploadImageByte(byteFile, fileName)){//保存文件到gridfs
//							urls.add(fileName);
//						}
//					}
					//使用普通方式  ftp上传
					if(content.getSimpleImage() != null){
						byte[] byteFile = HttpUtil.getImageByte(content.getSimpleImage());
						String fileName = StringUtil.cutOutUrlFileName(content.getSimpleImage());
						String foldler = StringUtil.dateToString(new Date(), "yyyyMMdd");
						String simpleImage = baseFileUploadService.saveFile(byteFile, fileName , foldler);
						resource.setSimpleImage(simpleImage);//图片保存成功后
						if(simpleImage != null){
							resource.setSimpleImageName( foldler + "/" + fileName);
							StringBuilder sb = new StringBuilder();
							for(String img : content.getImages()){
								sb.append(img+",");
							}
							resource.setImages(sb.toString());
						}
					}
					rlist.add(resource);
				}
		    	return crawlerService.saveForCheck(rlist);
		    default:
		    	return false;
		}
		
	}
	
	/**爬取器休息 */
	public void crawlerSheep(int time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
			logger.error("thread sheep error {}" ,e);
		}
	}
	
	/**
	 * 执行爬取操作
	 * @param message
	 */
	public void execute(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				startCrawle();
			}
		}).start();
	}
	
	/**
	 * 执行
	 */
	private void startCrawle(){
		CrawlerDescription crawlerDescription = this.getClass().getAnnotation(CrawlerDescription.class);
		/** 从注解中获得爬取的网站名字  */
		crawlerSite = crawlerDescription.crawlerSite();
		/** 从注解中获得爬取的数据类型 */
		crawlerType = crawlerDescription.crawlerType().getDescription();
		/** 从注解中获得爬取的网站匹配的url */
	    crawlerMatchUrl = crawlerDescription.crawlerMatchUrl();
		while(true){
			try {
				if(checkExecute()){ // 子类的注入是否执行
					if(this.threadGroup.activeCount() == 0){ //没有正在执行的线程
						logger.info("开始爬取  {} 论坛  -- {} 资源  ",crawlerSite,crawlerType);
					    new Thread(this.threadGroup,new Runnable() {
							@Override
							public void run() {
								while(true){
									executeCrawler(crawlerMatchUrl);
								    logger.info("爬取 {} 论坛 ---- {} 资源 操作 完成 线程进入休眠",crawlerSite,crawlerType);
								    crawlerSheep(SHEEP_TIME * 10);
								}
							}
						}).start();
					    state = CrawlerState.RUNNING;
					}else{
						logger.info("爬取 {} 论坛  --- {} 资源线程 正在执行中...",crawlerSite,crawlerType);
						crawlerSheep(SHEEP_TIME * 180);
					}
				}else{
					crawlerWait(crawlerSite,crawlerType);
				}
			}catch(Exception e){
				logger.error("爬取 {} 论坛  失败  异常信息：{}",crawlerSite+crawlerType,e);
				crawlerSheep(SHEEP_TIME * 30);
			}
		}
	}
	
	public synchronized void crawlerWait(String crawlerSite,String crawlerType) throws Exception{
		logger.info("系统配置不允许执行 爬取 {} 论坛 ---- {} 资源  操作被挂起",crawlerSite,crawlerType);
		state = CrawlerState.WAIT;
		this.wait();
		//logger.info("22系统配置不允许执行 爬取 {} 论坛 ---- {} 资源  操作被挂起",crawlerSite,crawlerType);//wait()后面的代码不会执行
	}
	
	public synchronized void crawlerNotify(){
		this.notifyAll();
		logger.info(" 爬取 {} 论坛 ---- {} 资源  操作线程被唤醒",crawlerSite,crawlerType);
	}
	
	/**
	 * 使用反射的方式获得是否执行
	 * @return
	 * @throws Exception
	 */
//	private boolean checkExecute() throws Exception{
//		Field field = this.getClass().getDeclaredField("execute");
//		field.setAccessible(true); // java.lang.IllegalAccessException 
//		return field.getBoolean(this);
//	}
	
	/**
	 * 判断是否执行
	 * @return
	 */
	public abstract boolean checkExecute();
	
	/**
	 * 执行或停止爬取器
	 * @param runable
	 */
	public void setExecute(boolean runable){
		if(!runable){
			com.mxk.crawler.model.CrawlerState state = new com.mxk.crawler.model.CrawlerState();
			state.setCrawlerName(this.getClass().getName());
			state.setCrawlerSiteName(crawlerSite);
			state.setCrawlerSiteUrl(crawlerMatchUrl);
			state.setLastExecuteTime(StringUtil.dateToString(new Date()));
			crawlerService.saveOrUpdateCrawlerState(state);
		}
	}
	
	/**
	 * 执行爬取操作
	 * 
	 */
	public void executeCrawler(String matchUrl){
		try{
			int allpage = crawlerService.findAllLinkPage(matchUrl);//获得总页数
			if(allpage == 0){
				logger.info("没有可以爬取的链接 爬取 {} 论坛 ---- {} 资源睡眠",crawlerSite,crawlerType);
				crawlerSheep(1000 * 60);
			}
			/** 计数器 超过5次失败就睡眠 */
			int count = 0;
			for(int i=1; i<=allpage; i++){
	    		List<Links> list = crawlerService.findLinkByPage(i,matchUrl);//获得需要爬取的link
	    		logger.info("加载匹配：{} 链接数量：{}",matchUrl,list.size());
	    		for(Links link : list){
	    			List<? extends BaseResource> resource = crawler(link.getUrl());
	    			if(resource.size() > 0){ //保存资源
	    				logger.info("获得 匹配链接 ：{} 下资源数量 ：{}",matchUrl,resource.size());
	    				//组装数据 对于那些在帖子页面拿不到评论数量和阅读数量的网站
	    				if(CrawleType.CONTENT.getDescription().equals(crawlerType)){
	    					for (BaseResource b : resource) {
		    					packageData(link, (Content) b);
		    				}
	    				}
	    				if(saveResource(resource)){ //被爬取的链接
	    					count++;
	    					if(count > 5){
	    						count = 0;
	    						crawlerSheep(3000 * 10); //持续出现没有保存资源说明没有可以爬取的了 就睡眠一段时间
	    					}
	    				}
	    				crawlerService.updateLinkState(link.getUrl(), ResourceState.CRAWLERD.getCode());
	    			}else{ //练级没有资源
	    				crawlerService.updateLinkState(link.getUrl(), ResourceState.LINK_NO_RESOURCE.getCode());
	    			}
	    		}
			}	
		}catch(Exception e){
			logger.error("爬取数据出现异常 异常信息：{} 链接{}",e,crawlerSite+"|"+this.getClass().getName()+"|"+matchUrl);
			crawlerSheep(5000);
		}
	}

	/**
	 * 爬取器状态
	 * @return
	 */
	public String getState() {
		return "爬取网站："+crawlerSite+" - "+crawlerType + " 爬取器目前状态：" + state.getCode();
	}
		
	/**
	 * 组装数据 子类重写次方法 主要是用于将link中 multiData 数据组装到 resource中
	 */
	public void packageData(Links link,Content resource){
		
	}	
}

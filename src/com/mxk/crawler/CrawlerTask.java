package com.mxk.crawler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.mxk.crawler.base.Crawler;
import com.mxk.crawler.base.CrawlerStateInfo;
import com.mxk.dao.BaseLinkMapper;
import com.mxk.model.BaseLink;
import com.mxk.model.BaseLinkCriteria;
import com.mxk.system.SystemService;
/**
 * 
 * @author Administrator
 *
 */
@Service
public class CrawlerTask implements ApplicationContextAware {

	public static final Logger logger = LoggerFactory.getLogger(CrawlerTask.class);
	
	public static final int SHEEP_TIME = 5000;
	
	@Resource
	private BaseLinkMapper baseLinkMapper;
	
	@Resource
	private SystemService systemService;
	
	/** 所有爬取器 */
	private List<Crawler> crawlers = new ArrayList<Crawler>();
	
	/**
	 * 开始爬取数据 spring初始化完成后开始爬取
	 */
	@PostConstruct 
    public void startCrawlerTask(){
		systemService.loadBaseUrl();
    	for ( Crawler crawler : crawlers ) {
    		synchronized(crawler){
    		   crawler.execute();
    		}
    	}
    }
    
	/**
	 * 唤醒挂起的爬取线程
	 */
	public synchronized void notifyAllCrawlerTask(String crawlerName, boolean runable){
		for ( Crawler crawler : crawlers ) {
			if(crawler.getClass().getName().equals(crawlerName)){
				crawler.setExecute(runable);
				crawler.crawlerNotify();
			}
    	}
	}
	
	/**
	 * 获得爬取器状态
	 * @return
	 */
	public List<CrawlerStateInfo> getCrawlerStateInfo(){
		List<CrawlerStateInfo> list = new ArrayList<CrawlerStateInfo>();
		for (Crawler crawler : crawlers) {
			CrawlerStateInfo info = new CrawlerStateInfo();
			info.setCrawlerName(crawler.getClass().getName());
			info.setInfo(crawler.getState());
			list.add(info);
		}
		return list;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		logger.info("开始加载爬取器和资源处理器");
		String[] crawlerArray = applicationContext.getBeanNamesForType(Crawler.class);
		for (String crawler : crawlerArray) {
			crawlers.add(applicationContext.getBean(crawler,Crawler.class));
		}
		logger.info("爬取器和资源处理器加载完成");
	}

	
	public void sheep(int time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
			logger.error("thread sheep error {}" ,e);
		}
	}
	
}

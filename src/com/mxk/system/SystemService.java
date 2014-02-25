package com.mxk.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.mxk.crawler.CrawlerService;
import com.mxk.crawler.model.LinkType;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;
import com.mxk.crawler.model.UserActionLog;
import com.mxk.dao.BaseLinkMapper;
import com.mxk.model.BaseLink;
import com.mxk.model.BaseLinkCriteria;
/**
 * 系统的基础参数服务
 * @author Administrator
 *
 */
@Service
public class SystemService {

	public static final Logger logger = LoggerFactory.getLogger(SystemService.class);
	
	@Autowired
	private MongoOperations mog; 
	
	@Resource
	private BaseLinkMapper baseLinkMapper;
	
	@Autowired
	private CrawlerService crawlerService;
	
	/**
	 * 加载基础的链接地址 
	 */
	@PostConstruct 
	public void loadBaseUrl(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadBaseLinkData();
			}
		}).start();
		
	}
	
	public void loadBaseLinkData(){
		logger.debug(" 加载基础的链接地址 开始");
		BaseLinkCriteria criteria = new BaseLinkCriteria();
		criteria.createCriteria().andIdGreaterThan(0);
		List<BaseLink> list = baseLinkMapper.selectByExample(criteria);
		List<Links> llist = new ArrayList<Links>();
		for(BaseLink base : list){
			Links link = new Links();
			link.setCreateTime(new Date());
			link.setUrl(base.getUrl());
			link.setMatchUrl(base.getMatchurl());
			link.setState(ResourceState.NO_CRAWLER.getCode());
			link.setLinkType(LinkType.ROOT_LINK.getCode());//是根路径
			llist.add(link);
		}
		crawlerService.saveLink(llist);
		logger.debug("加载基础的链接地址 完成");
	}
	
	/**
	 * 保存用户在网上的各个操作请求日志
	 * @param userActionLog
	 */
	public void saveUserActionLog(UserActionLog userActionLog){
		mog.save(userActionLog);
	}
	
	
}

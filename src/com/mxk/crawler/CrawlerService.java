package com.mxk.crawler;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mxk.crawler.model.BaseResource;
import com.mxk.crawler.model.ContentResource;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;


@Service
public class CrawlerService {

	public static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);
	
	@Autowired
	private MongoOperations mog; 
	
	@Value("${system.crawler.link.pagesize}")
	private int pageSize;
	
	public boolean saveForCheck(Collection<? extends BaseResource> t){
		logger.info("保存资源开始");
		boolean allfail = true;//当所有被保存资源都失败返回失败false
		for(BaseResource resource : t){
			ContentResource res = (ContentResource)resource;
			if(res.getSimpleImage() == null){ //没有图片就不保存
				updateLinkState(res.getLinkurl(),ResourceState.LINK_NO_RESOURCE.getCode());//标记链接为没有资源的链接
				continue;
			}
		    Query q = new Query(Criteria.where("linkurl").is(res.getLinkurl()));
		    long count = mog.count(q, resource.getClass());
		    if(count == 0){
		    	mog.save(res);
		    	allfail = false;
		    	logger.info("保存资源完成：{}",res.getHeadline());
		    	updateLinkState(res.getLinkurl(),ResourceState.CRAWLERD.getCode());//标记链接已经被爬取过了 
		    }else{
		    	logger.info("资源重复无法保存：{}",res.getHeadline());
		    }
		}
		return allfail;
	}
	
	/**
	 * 判断资源是否存在
	 * @param url
	 * @return
	 */
	public boolean checkResourceExist(String url){
		Query q = new Query(Criteria.where("linkurl").is(url));
	    long count = mog.count(q, ContentResource.class);
	    if(count == 0){
	    	return true;
	    }else{
	    	return false;
	    }
	}

	/**
	 * 保存link
	 */
	public boolean saveLink(List<? extends BaseResource> list){
		logger.info("save links start");
		boolean allfail = true;//当所有被保存链接都失败返回失败false
		for (BaseResource resource : list) {
			Links link = (Links) resource;
			Query q = new Query(Criteria.where("url").is(link.getUrl()));
		    long count = mog.count(q, Links.class);
		    if(count == 0){
		    	mog.save(link);
		    	allfail = false;
		    	logger.info("保存 links：{}",link.getUrl());
		    }else{
		    	logger.info("链接重复无法保存：{}",link.getUrl());
		    }
		}
		return allfail;
	}
	
	public void updateLinkState(String url,int state){
		Query q = new Query(Criteria.where("url").is(url));
		Update u = new Update();
		u.set("state", state);
		mog.updateMulti(q ,u, Links.class);
		logger.info("链接状态被修改：{} 目前状态：{}",url,state);
	}
	
	
	public List<Links> findLinkByPage(int page,String matchUrl){
		Query q = new Query(Criteria.where("state").is(ResourceState.NO_CRAWLER.getCode()).and("matchUrl").is(matchUrl));
		q.limit(pageSize);
		q.skip(pageSize*(page - 1));
		q.sort().on("createTime", Order.DESCENDING);
		return mog.find(q , Links.class);
	}
	
	public int findAllLinkPage(String matchUrl){
		Query q = new Query(Criteria.where("state").is(ResourceState.NO_CRAWLER.getCode()).and("matchUrl").is(matchUrl)); //查找拼配的爬取数据
		int count = (int) mog.count(q, Links.class);
		if(count != 0){
			return (count + pageSize - 1) / pageSize;
		}else{
			return 0;
		}
	}
}

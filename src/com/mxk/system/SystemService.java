package com.mxk.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mxk.crawler.CrawlerService;
import com.mxk.crawler.CrawlerTask;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;
import com.mxk.crawler.model.ResourceType;
import com.mxk.crawler.model.SortableComparator;
import com.mxk.crawler.model.Tag;
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
	 * 保存标签
	 * @param tag
	 */
	public void saveTag(Tag tag){
		mog.save(tag);
	}
	
	public void saveSubTag(){
		
	}
	
	/**
	 * 加载基础的链接地址 
	 */
	@PostConstruct 
	public void loadBaseUrl(){
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
			llist.add(link);
		}
		crawlerService.saveLink(llist);
		logger.debug(" 加载基础的链接地址 完成");
	}
	
	
	/**
	 * 查询标签
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> findTagsByType(ResourceType type){
		Query q = new Query(Criteria.where("type").is(type.getCode()));
		q.sort().on("sort", Order.DESCENDING); //
		List<Tag> list = mog.find(q, Tag.class);
		if(!list.isEmpty()){
			for(Tag tag : list){
				SortableComparator comparator = new SortableComparator(); //排序 内嵌文档不建议使用monogdb排序
				Collections.sort(tag.getSubtags(), comparator);
				/**
				 * 使用  db.xxx.aggregate([{
                 *    $match: {'game.gid': 02569}
                 *    },{
                 *    $sort: {$date: -1}
                 *    }])
				 */
			}
		}
		return list;
	}
}

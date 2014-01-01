package com.mxk.resource.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mxk.crawler.model.ContentResource;
import com.mxk.crawler.model.ResourceState;
import com.mxk.crawler.model.WebResource;

@Component
public class ResourceDao {

	@Autowired
	private MongoOperations mog; 

	/**
	 * 保存一个资源 和支援对应的标签
	 * @param res
	 * @param ids
	 */
	public void saveResource(WebResource res){
		mog.save(res);
	}
	
	/**
	 * 模糊查询互联网资源
	 * @param keyword
	 * @return
	 */
	public List<WebResource> findResourceByKeyWord(String keyword){
		Query q = new Query(Criteria.where("keyword").regex(".*?"+keyword+".*"));
		q.limit(10);
		return mog.find(q, WebResource.class);
	}
	
	/**
	 * 查询没有编目的帖子
	 * @return
	 */
	public List<ContentResource> findContextResourceNotCatalog(){
		Query q = new Query(Criteria.where("state").is(ResourceState.NO_CATALOGO.getCode()));
		q.limit(50);
		return mog.find(q, ContentResource.class);
	}
	
	/**
	 * 查询没有编目的帖子 匹配
	 * @return
	 */
	public List<ContentResource> findContextResourceNotCatalogRegex(String keyword){
		Query q = new Query(Criteria.where("state").is(ResourceState.NO_CATALOGO.getCode()).and("headline").regex(".*?"+keyword+".*"));
		q.limit(50);
		return mog.find(q, ContentResource.class);
	}
	
//	public List<Resource> findResourceByTags(ResourceType type,String[] tagsId){
//		//Query q = new Query(Criteria.where("type").is(type.toString()).and("tags").in(tags));
//		//先分页查询
//		
//		return  mog.find(q, Resource.class);
//    }
//	
//	public 
	
}

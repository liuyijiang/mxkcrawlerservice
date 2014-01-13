package com.mxk.resource.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
	
	/**
	 * 编目帖子
	 * @param id
	 * @param resid
	 */
	public void catalogContext(String id,String resid){
		Query q = new Query(Criteria.where("id").is(id));
		Update u = new Update();
		u.set("resourceId", resid);
		u.set("state", ResourceState.CATALOGOED.getCode());
		u.set("recommend", (int)(Math.random()* 100));
		int n1 = 50 + (int)(Math.random()* 50);
		int n2 = 50 + (int)(Math.random()* 50);
		int n3 = 50 + (int)(Math.random()* 50);
		int n4 = 50 + (int)(Math.random()* 50);
		int n5 = 50 + (int)(Math.random()* 50);
		int points[] = new int[]{n1,n2,n3,n4,n5};
		u.set("points", points);
		u.set("sumary", (n1+n2+n3+n4+n5) / 5);
		mog.updateMulti(q, u,ContentResource.class);
		Query q2 = new Query(Criteria.where("id").is(resid));
		Update u2 = new Update();
		u2.inc("quantity", 1);
		mog.updateMulti(q2, u2,WebResource.class);
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

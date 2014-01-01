package com.mxk.resource.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mxk.crawler.CrawlerService;
import com.mxk.crawler.model.ContentResource;
import com.mxk.crawler.model.WebResource;
import com.mxk.resource.dao.ResourceDao;
/**
 * 
 * @author Administrator
 *
 */
/**
 * 
 * @author Administrator
 *
 */
@Service
public class ResourceService {

    public static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);
	
	@Autowired
	private ResourceDao dao; 
	
	/**
	 * 保存互联网资源
	 * @param res
	 */
	public void saveResource(WebResource res){
		dao.saveResource(res);
	}
	
	public List<WebResource> findResourceByKeyWord(String keyword){
		return dao.findResourceByKeyWord(keyword);
	}
	
	public List<ContentResource> findContextResourceNotCatalog(){
		return dao.findContextResourceNotCatalog();
	}
	
	public List<ContentResource> findContextResourceNotCatalogRegex(String key){
		return dao.findContextResourceNotCatalogRegex(key);
	}
	
//	public List<Resource> findResourceByTags(ResourceType type,String[] tags){
//		return dao.findResourceByTags(type, tags);
//	}

}

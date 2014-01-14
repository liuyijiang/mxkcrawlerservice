package com.mxk.web.resource;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mxk.crawler.model.ContentResource;
import com.mxk.dao.WebResourceMapper;
import com.mxk.model.WebResource;

/**
 * 资源处理器
 * @author Administrator
 *
 */
@Service
public class WebResourceService {

	@Resource
	private WebResourceMapper webResourceMapper;
	
	public void saveWebResource(ContentResource contentResource){
		WebResource web = createWebResource(contentResource);
		try{
		   webResourceMapper.insert(web);
		}catch(Exception e){
			throw  new RuntimeException();
		}
	}
	
	private WebResource createWebResource(ContentResource contentResource){
		WebResource web = new WebResource();
		web.setImage(contentResource.getSimpleImageName());
		web.setInfo(contentResource.getInfo());
		web.setInsignificance((int)(Math.random()*100));
		web.setMultiinfo(contentResource.getMultiData()+"评论："+contentResource.getPost() + "阅读：" + contentResource.getHit());
		web.setOwner(contentResource.getOwner());
		web.setPosts(Integer.parseInt(contentResource.getPost()));
		web.setReads(Integer.parseInt(contentResource.getHit()));
		web.setSignificance((int)(Math.random()*100));
		web.setSitename(contentResource.getSitename());
		web.setSiteurl(contentResource.getSiteurl());
		web.setTitle(contentResource.getHeadline());
		web.setUrl(contentResource.getLinkurl());
		return web;
	}
	
	
}

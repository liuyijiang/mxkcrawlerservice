package com.mxk.web.resource;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mxk.crawler.model.ContentResource;
import com.mxk.dao.WebResourceMapper;
import com.mxk.model.WebResource;
import com.mxk.model.WebResourceCriteria;
import com.mxk.util.StringUtil;
import com.mxk.web.http.ServiceResponse;

/**
 * 资源处理器
 * @author Administrator
 *
 */
@Service
public class WebResourceService {

	public static final Logger logger = LoggerFactory.getLogger(WebResourceService.class);
	
	@Resource
	private WebResourceMapper webResourceMapper;
	
	/**
	 * 保存资源
	 * @param contentResource
	 * @return
	 */
	public int saveWebResource(ContentResource contentResource){
		int message = ServiceResponse.SUCCESS.getCode();
		WebResource web = createWebResource(contentResource);
		try{
			WebResourceCriteria criteria = new WebResourceCriteria();
			criteria.createCriteria().andUrlEqualTo(web.getUrl());
			if(webResourceMapper.selectByExample(criteria).size() == 0){
				webResourceMapper.insert(web);
			}else{
				message = ServiceResponse.DATA_REPETITION.getCode();
			}
		}catch(Exception e){
			message = ServiceResponse.SERVICE_ERROR.getCode();
			logger.error("保存资源异常{}",e);
		}
		return message;
	}
	
	private WebResource createWebResource(ContentResource contentResource){
		WebResource web = new WebResource();
		web.setImage(contentResource.getSimpleImageName());
		web.setInfo(contentResource.getInfo());
		web.setInsignificance((int)(Math.random()*100));
		String post = "";
		String hits = "";
		if(!StringUtil.stringIsEmpty(contentResource.getPost())){
			post = " 评论：" + contentResource.getPost();
		}
		if(!StringUtil.stringIsEmpty(contentResource.getHit())){
			hits = " 阅读：" + contentResource.getHit();
		}
		web.setMultiinfo(StringUtil.toEnpty(contentResource.getMultiData()) + post + hits);
		web.setOwnername(contentResource.getOwner());
		web.setPosts(Integer.parseInt(contentResource.getPost()));
		web.setHits(Integer.parseInt(contentResource.getHit()));
		web.setSignificance((int)(Math.random()*100));
		web.setSitename(contentResource.getSitename());
		web.setSiteurl(contentResource.getSiteurl());
		web.setTitle(contentResource.getHeadline());
		web.setUrl(contentResource.getLinkurl());
		return web;
	}
	
	
}

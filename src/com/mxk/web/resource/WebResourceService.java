package com.mxk.web.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mxk.crawler.model.ContentResource;
import com.mxk.dao.WebResourceMapper;
import com.mxk.model.WebResource;
import com.mxk.model.WebResourceCriteria;
import com.mxk.util.StringUtil;
import com.mxk.web.http.ServiceResponse;
import com.mxk.web.search.PageModel;
import com.mxk.web.search.SearchRespone;

/**
 * 资源处理器
 * @author Administrator
 *
 */
@Service
public class WebResourceService {

	@Value("${system.web.execute.load.new.webresource}")
	private boolean execute;
	
	public static final Logger logger = LoggerFactory.getLogger(WebResourceService.class);
	
	//public static BlockingQueue<String> NEW_WEB_RESOURCE_QUEUE = new LinkedBlockingQueue<String>(3);
	
	private static ConcurrentLinkedQueue<WebResource> NEW_WEB_RESOURCE_QUEUE = new ConcurrentLinkedQueue<WebResource>();
	
	@Resource
	private WebResourceMapper webResourceMapper;
	
	@Resource
	private WebResourceMapperPlus webResourceMapperPlus;
	
	
	/**
	 * 系统启动的时候开启一个后台程序 实时更新爬取的最新数据
	 */
	@PostConstruct 
	public void initWebResourceService() {
		if(execute){
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					logger.debug("开始加载最新的webresource");
					try{
						int maxid = webResourceMapperPlus.selectMaxId();
					    if(maxid > 20){
					    	maxid = maxid - 20;
					    }
					    List<WebResource> list = webResourceMapperPlus.selectlimit(maxid);
					    updateNewWebResource(list);//初始化最新值
					    //Thread.sleep(1000 * 60 * 3);
					    Thread.sleep(1000 * 20);
					    maxid = maxid + 20;
							while(true){
								list = webResourceMapperPlus.selectlimit(maxid);
								updateNewWebResource(list);//初始化最新值
								maxid = maxid + list.size();
								logger.debug("###########开始加载最新的webresoource max id{}",maxid);
								//Thread.sleep(1000 * 60 * 3);
								Thread.sleep(1000 * 30);
							}
					}catch(Exception e){
						logger.error("更新最新数据出现异常 {}",e);
						//TODO 邮件提示
					}
				}
				
			});
			t.setDaemon(true);
			t.start();
		}
	}
	
	private void updateNewWebResource(List<WebResource> list){
		for (WebResource web : list){
			if(NEW_WEB_RESOURCE_QUEUE.size() >= 20){
				NEW_WEB_RESOURCE_QUEUE.poll();
			}
			NEW_WEB_RESOURCE_QUEUE.add(web);
		}
	}
	
	public PageModel getNewWebResource(){
		PageModel model = new PageModel();
		List<SearchRespone> rlist = new ArrayList<SearchRespone>();
		for (WebResource web : NEW_WEB_RESOURCE_QUEUE){
		     SearchRespone sr = new SearchRespone();
		     sr.setImg(web.getImage());
		     sr.setTitle(web.getTitle());
		     sr.setSubtext(web.getMultiinfo());
		     sr.setInfo(StringUtil.subString(web.getInfo(), 300));
		     sr.setUrl(web.getUrl());
		     sr.setId(web.getId().toString());
			 rlist.add(sr);
		}
		model.setTotal(1);
        model.setCurrentPage(1);
        model.setPage(1);
        model.setData(rlist);
        return model;
	}
	
	
	public WebResource selectByWebResourceId(int id){
		return webResourceMapper.selectByPrimaryKey(id);
	}
	
	private LoadingCache<String, Integer> graphs = CacheBuilder.newBuilder()
			.maximumSize(1000)
			.build(new CacheLoader<String, Integer>() {
				public Integer load(String key) {
					return webResourceMapperPlus.selectMaxId();
				}
	});
	
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
		if(contentResource.getInfo().length() > 4500){
			web.setInfo(contentResource.getInfo().substring(0,4000));
		}else{
			web.setInfo(contentResource.getInfo());
		}
		web.setInsignificance((int)(Math.random()*100));
		String post = "";
		String hits = "";
		if(!StringUtil.stringIsEmpty(contentResource.getPost())){
			post = " 评论：" + contentResource.getPost();
			web.setPosts(Integer.parseInt(contentResource.getPost()));
		}
		if(!StringUtil.stringIsEmpty(contentResource.getHit())){
			hits = " 阅读：" + contentResource.getHit();
			web.setHits(Integer.parseInt(contentResource.getHit()));
		}
		web.setImages(contentResource.getImages());
		web.setMultiinfo(StringUtil.toEnpty(contentResource.getMultiData()) + post + hits);
		web.setOwnername(contentResource.getOwner());
		web.setSignificance((int)(Math.random()*100));
		web.setSitename(contentResource.getSitename());
		web.setSiteurl(contentResource.getSiteurl());
		web.setTitle(contentResource.getHeadline());
		web.setUrl(contentResource.getLinkurl());
		return web;
	}
	
	/**
	 * 获得数据库中所有资源数量
	 * @return
	 */
	public int maxWebResourceId() {
		int maxId = 0;
		try{
			maxId = graphs.get("max");
		}catch(Exception e){
			logger.error("获得数据库中所有资源数量异常{}",e);
		}
		return maxId;
	}
	
	//TODO 缓存
	/**
	 * 查询资源by ids
	 * @param ids
	 * @return
	 */
	public List<WebResource> findWebResourceByIds(List<Integer> ids){
		WebResourceCriteria criteria = new WebResourceCriteria();
		criteria.createCriteria().andIdIn(ids);
		List<WebResource> list = webResourceMapper.selectByExample(criteria);
		return list;
	}
	
	/**
	 * 更新资源的评价
	 * @param significance
	 * @return
	 */
	public void updateWebResourceSignificance(int id,boolean significance){
		if(significance){
			webResourceMapperPlus.updateSignificance(id);
		}else{
			webResourceMapperPlus.updateInSignificance(id);
		}
	}
	
}

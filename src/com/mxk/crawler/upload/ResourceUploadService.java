package com.mxk.crawler.upload;

import java.util.List;

import javax.annotation.Resource;

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

import com.mxk.crawler.model.ContentResource;
import com.mxk.crawler.model.Links;
import com.mxk.crawler.model.ResourceState;

/**
 * 数据同步service    
 * 服务可控的将爬取出来的内容和图片上传到生产环境 
 * @author Administrator
 *
 */
@Service
public class ResourceUploadService {

	public static final Logger logger = LoggerFactory.getLogger(ResourceUploadService.class);
	
	@Resource
	private FtpService ftpService;
	
	/** 是否执行 */
	@Value("${resource.upload.exe}")
	private boolean exe;
	
	/** 文件保存的根路径 */
	@Value("${file.path}")
	private String rootpath;
	
	@Autowired
	private MongoOperations mog; 
	
	/**
	 * 
	 */
	public void execute(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					if(exe){
						logger.info("同步数据服务执行");
						
						
					}else{
						logger.debug("同步数据服务未允许执行线程睡眠1分钟");
						serviceSheep(1000 * 60);
					}
				}
			}
		}).start();
	}
	
	private void upload(){
		List<ContentResource> list = loadNoUploadData();
		for (ContentResource cr : list) {
			
		}
	}
	
	
	/**
	 * 取50条没有被编目的资源
	 * @return
	 */
	private List<ContentResource> loadNoUploadData(){
		Query q = new Query(Criteria.where("state").is(ResourceState.NO_CATALOGO.getCode()));//
		q.limit(50);
		return mog.find(q,ContentResource.class);
	}
	
	/**
	 * 修改状态
	 * @param id
	 * @param state
	 */
	private void ContentResourceState(String id,int state){
		Query q = new Query(Criteria.where("id").is(id));
		Update u = new Update();
		u.set("state", state);
		mog.updateMulti(q ,u, ContentResource.class);
	}
	
	
	public void serviceSheep(int time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
			logger.error("thread sheep error {}" ,e);
		}
	}
	
}

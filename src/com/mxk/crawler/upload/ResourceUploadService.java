package com.mxk.crawler.upload;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mxk.crawler.model.ContentResource;
import com.mxk.crawler.model.ResourceState;
import com.mxk.util.BeanUtil;
import com.mxk.web.http.ServiceResponse;

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
	@Value("${resource.upload.url}")
	private String uploadUrl;
	
	@Autowired
	private MongoOperations mog; 
	
	/**
	 * 同步数据
	 */
	@PostConstruct 
	public void execute(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					if(exe){
						logger.info("同步数据服务执行");
						List<ContentResource> list = loadNoUploadData();
						if(list.size() != 0){
							upload(list);
						}else{
							logger.info("暂无需要同步的数据");
							serviceSheep(1000 * 60);
						}
						logger.info("同步数据服务完成");
						serviceSheep(1000 * 3);
					}else{
						logger.debug("同步数据服务未允许执行线程睡眠1分钟");
						serviceSheep(1000 * 60);
					}
				}
			}
			
		}).start();
	}
	
	/**
	 * 启动或停止
	 */
    public void startOrStopUpload(){
    	if(exe){
    		this.exe = false;
    	}else{
    		this.exe = true;
    	}
    }
	
    /**
     * 同步数据
     * @param list
     */
	private void upload(List<ContentResource> list){
		for (ContentResource cr : list) {
			String fileinfo = cr.getSimpleImageName();
			String folder = fileinfo.substring(0,fileinfo.indexOf("/"));
			String filename = fileinfo.substring(fileinfo.indexOf("/") + 1 ,fileinfo.length());
			//if(ftpService.uploadFile(cr.getSimpleImage(), filename , folder)){
				Map<String,Object> value = BeanUtil.transBeanToMap(cr);
				if(callService(value)){
					refreshResourceState(cr.getId(),ResourceState.UPLOADED.getCode());
				}
				logger.error("完成资源上传{}" ,JSON.toJSONString(value));
			//}
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
	private void refreshResourceState(String id,int state){
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
	
	/**
	 * 模拟post请求
	 * @param url
	 * @param value
	 */
	public boolean callService (Map<String,Object> value) {
		boolean success = false;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			String data = encodeParams(value);
			StringEntity s = new StringEntity(data);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/x-www-form-urlencoded");   
			HttpPost httppost = new HttpPost(uploadUrl);
			httppost.setEntity(s);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String respone = EntityUtils.toString(entity);
				Integer code = Integer.parseInt(respone);
				//当保存成功或者重复的时候
				if(ServiceResponse.SUCCESS.getCode().equals(code) || ServiceResponse.DATA_REPETITION.getCode().equals(code)){
					success = true;
				}
			}
			httppost.abort();
		} catch (Exception e) {
			success = false;
			logger.error("请求服务器异常{} 请求地址和数据{}",e , (uploadUrl + JSON.toJSONString(value)));
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return success;
     }
	
	 private String encodeParams(Map<String,Object> parms) throws Exception{
    	if(parms == null){
    		return null;
    	}
    	StringBuffer sb = new StringBuffer();
    	for (Entry<String,Object> entry : parms.entrySet()) {
    		if(entry.getValue() != null){
    			String key = URLEncoder.encode(entry.getKey(),"UTF-8");
        		String value = URLEncoder.encode(entry.getValue().toString(),"UTF-8");
        		sb.append(key+"="+value+"&");
    		}
    	}
    	return sb.toString();
    }
	
	 
	 
	 
}

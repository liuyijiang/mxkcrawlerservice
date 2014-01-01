package com.mxk.crawler.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

/**
 * 全文检索索引服务
 * @author Administrator
 *
 */
@Service
public class IndexService {

    public static final Logger logger = LoggerFactory.getLogger(IndexService.class);
	
	@Autowired
	private MongoOperations mog; 
	
	public void createIndex(){
		
	}
	
}

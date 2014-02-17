package com.mxk.system.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mxk.dao.CatalogResourceMapper;

/**
 * 这是后台编辑整理互联网优秀资源的服务
 * @author Administrator
 *
 */
@Service
public class CatalogResourceService {
  
	public static final Logger logger = LoggerFactory.getLogger(CatalogResourceService.class);
	
	@Autowired
	private CatalogResourceMapper catalogResourceMapper;
	
	@Autowired
	private CatalogResourceMapperPlus catalogResourceMapperPlus;
	
	/**
	 * 保存CatalogResource
	 * @param catalogResourcePlus
	 * @return
	 */
	public boolean saveCatalogResource(CatalogResourcePlus  catalogResourcePlus){
		boolean success = true;
		try{
			catalogResourceMapper.insert(catalogResourcePlus);
		}catch(Exception e){
			logger.error("保存CatalogResource失败{}",e);
			success = false;
		}
		return success;
	}
	
}

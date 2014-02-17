package com.mxk.system.catalog;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import com.mxk.model.CatalogResource;

public interface CatalogResourceMapperPlus {

	@Insert("insert into tb_catalog_resource (name, tag, type, describes, create_time,image_url, hot) values ( #{name,jdbcType=VARCHAR}, #{tag,jdbcType=VARCHAR}, #{type,jdbcType=INTEGER}, #{describes,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, #{imageUrl,jdbcType=VARCHAR}, #{hot,jdbcType=INTEGER})")
	@Options(useGeneratedKeys = true, keyProperty = "id")  
	int insert(CatalogResource record);
	
}

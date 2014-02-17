package com.mxk.dao;

import com.mxk.model.CatalogResource;
import com.mxk.model.CatalogResourceCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CatalogResourceMapper {
    int countByExample(CatalogResourceCriteria example);

    int deleteByExample(CatalogResourceCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(CatalogResource record);

    int insertSelective(CatalogResource record);

    List<CatalogResource> selectByExample(CatalogResourceCriteria example);

    CatalogResource selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CatalogResource record, @Param("example") CatalogResourceCriteria example);

    int updateByExample(@Param("record") CatalogResource record, @Param("example") CatalogResourceCriteria example);

    int updateByPrimaryKeySelective(CatalogResource record);

    int updateByPrimaryKey(CatalogResource record);
}
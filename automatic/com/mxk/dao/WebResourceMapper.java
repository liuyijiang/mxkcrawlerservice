package com.mxk.dao;

import com.mxk.model.WebResource;
import com.mxk.model.WebResourceCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebResourceMapper {
    int countByExample(WebResourceCriteria example);

    int deleteByExample(WebResourceCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(WebResource record);

    int insertSelective(WebResource record);

    List<WebResource> selectByExample(WebResourceCriteria example);

    WebResource selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WebResource record, @Param("example") WebResourceCriteria example);

    int updateByExample(@Param("record") WebResource record, @Param("example") WebResourceCriteria example);

    int updateByPrimaryKeySelective(WebResource record);

    int updateByPrimaryKey(WebResource record);
}
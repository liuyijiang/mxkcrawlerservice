package com.mxk.dao;

import com.mxk.model.SearchLog;
import com.mxk.model.SearchLogCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SearchLogMapper {
    int countByExample(SearchLogCriteria example);

    int deleteByExample(SearchLogCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SearchLog record);

    int insertSelective(SearchLog record);

    List<SearchLog> selectByExample(SearchLogCriteria example);

    SearchLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SearchLog record, @Param("example") SearchLogCriteria example);

    int updateByExample(@Param("record") SearchLog record, @Param("example") SearchLogCriteria example);

    int updateByPrimaryKeySelective(SearchLog record);

    int updateByPrimaryKey(SearchLog record);
}
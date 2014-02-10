package com.mxk.dao;

import com.mxk.model.BaseLink;
import com.mxk.model.BaseLinkCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseLinkMapper {
    int countByExample(BaseLinkCriteria example);

    int deleteByExample(BaseLinkCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(BaseLink record);

    int insertSelective(BaseLink record);

    List<BaseLink> selectByExample(BaseLinkCriteria example);

    BaseLink selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BaseLink record, @Param("example") BaseLinkCriteria example);

    int updateByExample(@Param("record") BaseLink record, @Param("example") BaseLinkCriteria example);

    int updateByPrimaryKeySelective(BaseLink record);

    int updateByPrimaryKey(BaseLink record);
}
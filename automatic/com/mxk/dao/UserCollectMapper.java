package com.mxk.dao;

import com.mxk.model.UserCollect;
import com.mxk.model.UserCollectCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserCollectMapper {
    int countByExample(UserCollectCriteria example);

    int deleteByExample(UserCollectCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserCollect record);

    int insertSelective(UserCollect record);

    List<UserCollect> selectByExample(UserCollectCriteria example);

    UserCollect selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserCollect record, @Param("example") UserCollectCriteria example);

    int updateByExample(@Param("record") UserCollect record, @Param("example") UserCollectCriteria example);

    int updateByPrimaryKeySelective(UserCollect record);

    int updateByPrimaryKey(UserCollect record);
}
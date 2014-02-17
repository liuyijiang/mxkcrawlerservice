package com.mxk.dao;

import com.mxk.model.UserSubjectPart;
import com.mxk.model.UserSubjectPartCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserSubjectPartMapper {
    int countByExample(UserSubjectPartCriteria example);

    int deleteByExample(UserSubjectPartCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserSubjectPart record);

    int insertSelective(UserSubjectPart record);

    List<UserSubjectPart> selectByExample(UserSubjectPartCriteria example);

    UserSubjectPart selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserSubjectPart record, @Param("example") UserSubjectPartCriteria example);

    int updateByExample(@Param("record") UserSubjectPart record, @Param("example") UserSubjectPartCriteria example);

    int updateByPrimaryKeySelective(UserSubjectPart record);

    int updateByPrimaryKey(UserSubjectPart record);
}
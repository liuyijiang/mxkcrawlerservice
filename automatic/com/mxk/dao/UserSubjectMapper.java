package com.mxk.dao;

import com.mxk.model.UserSubject;
import com.mxk.model.UserSubjectCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserSubjectMapper {
    int countByExample(UserSubjectCriteria example);

    int deleteByExample(UserSubjectCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserSubject record);

    int insertSelective(UserSubject record);

    List<UserSubject> selectByExample(UserSubjectCriteria example);

    UserSubject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserSubject record, @Param("example") UserSubjectCriteria example);

    int updateByExample(@Param("record") UserSubject record, @Param("example") UserSubjectCriteria example);

    int updateByPrimaryKeySelective(UserSubject record);

    int updateByPrimaryKey(UserSubject record);
}
package com.mxk.web.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import com.mxk.model.UserSubjectPart;

public interface UserSubjectPartMapperPlus {

	@Insert("insert into tb_part (subject_id, user_id, img_url, subject_name, subject_type, create_time, part_info) values ( #{subjectId,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, #{imgUrl,jdbcType=VARCHAR}, #{subjectName,jdbcType=VARCHAR}, #{subjectType,jdbcType=VARCHAR},#{createTime,jdbcType=TIMESTAMP}, #{partInfo,jdbcType=VARCHAR})")
	@Options(useGeneratedKeys = true, keyProperty = "id")  
	int insert(UserSubjectPart record);
	
}

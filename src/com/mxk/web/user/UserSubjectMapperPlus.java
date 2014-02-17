package com.mxk.web.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mxk.model.UserSubject;

public interface UserSubjectMapperPlus {

	@Insert("insert into tb_subject (category, faceimage, title, tag, userid,createTime) values (#{category,jdbcType=VARCHAR}, #{faceimage,jdbcType=VARCHAR}, #{title,jdbcType=VARCHAR}, #{tag,jdbcType=VARCHAR}, #{userid,jdbcType=INTEGER},  #{createtime,jdbcType=TIMESTAMP})")
	@Options(useGeneratedKeys = true, keyProperty = "id")  
	int insert(UserSubject record);
	
	@Select("select count(1) from tb_subject where id = #{id} and userid = #{userid}")
	int findSubjectUserId(@Param("id") int id, @Param("userid") int userid);
	
	@Update("update tb_subject set faceimage = #{url} where id = #{id}")
	void updateUserSubjectImageUrl(String url,int id);
	
}

package com.mxk.web.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mxk.model.User;


/**
 * UserMapper补充
 * @author Administrator
 *
 */
public interface UserMapperPlus {

	@Select("select count(1) from mxkdatabase.tb_user where user_name =  #{name} ")
	int uniqueUserNameValiate(@Param("name") String name);
	
	@Select("select count(1) from mxkdatabase.tb_user where user_email = #{email} ")
	int uniqueUserEmailValiate(@Param("email") String email);
	
	@Insert("insert into mxkdatabase.tb_user (user_name, user_image, user_email, user_password) values (#{userName,jdbcType=VARCHAR}, #{userImage,jdbcType=VARCHAR}, #{userEmail,jdbcType=VARCHAR}, #{userPassword,jdbcType=VARCHAR})")
	@Options(useGeneratedKeys = true, keyProperty = "id")  
	int insert(User record);
	
}

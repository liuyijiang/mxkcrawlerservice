package com.mxk.web.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


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
}

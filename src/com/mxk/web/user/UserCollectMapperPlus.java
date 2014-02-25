package com.mxk.web.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mxk.model.UserCollect;

public interface UserCollectMapperPlus {

	@Select("select * from mxkdatabase.tb_user_collect where id >= #{id} and user_id = #{userid} limit 40")
	List<UserCollect> getUserCollectByPage(@Param("userid") int userid,@Param("id") int id);
	
	@Select("select min(id) from mxkdatabase.tb_user_collect where user_id = #{userid} ")
	int minUserCollectId(@Param("userid") int userid);
	
}

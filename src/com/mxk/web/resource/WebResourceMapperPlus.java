package com.mxk.web.resource;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mxk.model.WebResource;

public interface WebResourceMapperPlus {
    //${} 硬替换有风险sql注入
	//#{} 参数替换安全
	
	@Select("select * from mxkdatabase.tb_web_resource where id > ${startid} and id <= ${startid + 100 } limit 100")
	List<WebResource> selectlimit(@Param("startid") int startid);
	
	@Select("select max(id) from mxkdatabase.tb_web_resource ")
	int selectMaxId();
	
	@Update("update mxkdatabase.tb_web_resource set significance = (significance + 1) where id = #{id} ")
	void updateSignificance(@Param("id") int id);
	
	@Update("update mxkdatabase.tb_web_resource set insignificance = (insignificance + 1) where id = #{id} ")
	void updateInSignificance(@Param("id") int id);
	
}

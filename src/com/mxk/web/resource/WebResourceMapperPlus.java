package com.mxk.web.resource;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mxk.model.WebResource;

public interface WebResourceMapperPlus {

	@Select("select * from mxkdatabase.tb_web_resource where id > ${startid} and id <= ${startid + 100 } limit 100")
	List<WebResource> selectlimit(@Param("startid") int startid);
	
}

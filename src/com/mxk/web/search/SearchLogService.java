package com.mxk.web.search;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mxk.dao.SearchLogMapper;
import com.mxk.filter.CourntThreadUserMessageFilter;
import com.mxk.filter.CourntThreadUserMessageFilter.CourntThreadUserMessage;
import com.mxk.model.SearchLog;
import com.mxk.web.base.ClientInfo;

@Service
public class SearchLogService {
   
	@Resource
	private SearchLogMapper searchLogMapper;
	
	/**
	 * 保存用户搜索的信息 为下次进入推荐
	 * @param keywrod
	 */
	public void saveUserSearchInfo(String keywrod){
		CourntThreadUserMessage message = CourntThreadUserMessageFilter.getCourntThreadUserMessage();
		SearchLog searchLog = new SearchLog();
		searchLog.setCreateTime(new Date());
		searchLog.setKeyword(keywrod);
		searchLog.setSearchFormIp(message.ip);
		searchLog.setSearchFromSite(ClientInfo.WEB.getCode());
		searchLog.setSearchFromUser(message.userid);
		searchLogMapper.insert(searchLog);
	}
	
}

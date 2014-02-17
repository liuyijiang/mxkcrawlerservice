package com.mxk.web.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mxk.dao.SearchLogMapper;
import com.mxk.filter.CourntThreadUserMessageFilter;
import com.mxk.filter.CourntThreadUserMessageFilter.CourntThreadUserMessage;
import com.mxk.model.SearchLog;
import com.mxk.model.WebResource;
import com.mxk.util.StringUtil;
import com.mxk.web.base.ClientInfo;
import com.mxk.web.resource.WebResourceService;

@Service
public class SearchService {
   
	@Resource
	private SearchLogMapper searchLogMapper;
	
	@Autowired
	private WebResourceService webResourceService;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
	/**
	 * 保存用户搜索的信息 为下次进入推荐
	 * @param keywrod
	 */
	public void saveUserSearchInfo(final String keywrod){
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				CourntThreadUserMessage message = CourntThreadUserMessageFilter.getCourntThreadUserMessage();
				SearchLog searchLog = new SearchLog();
				searchLog.setCreateTime(new Date());
				searchLog.setKeyword(keywrod);
				searchLog.setSearchFormIp(message.ip);
				searchLog.setSearchFromSite(ClientInfo.WEB.getCode());
				searchLog.setSearchFromUser(message.userid);
				searchLogMapper.insert(searchLog);
			}
		});
	}
	
	/**
	 * 随机的查询10条记录
	 * @return
	 */
	public PageModel luckSearch(){
		PageModel model = new PageModel();
		List<Integer> ids = new ArrayList<Integer>();
		int max = webResourceService.maxWebResourceId();
		for(int i=0;i<10;i++){
			int num = (int)(max * Math.random());
			ids.add(num);
		}
		List<WebResource> list = webResourceService.findWebResourceByIds(ids);
		List<SearchRespone> rlist = new ArrayList<SearchRespone>();
		for (WebResource web : list){
		     SearchRespone sr = new SearchRespone();
		     sr.setImg(web.getImage());
		     sr.setTitle(web.getTitle());
		     sr.setSubtext(web.getMultiinfo());
		     sr.setInfo(StringUtil.subString(web.getInfo(), 300));
		     sr.setUrl(web.getUrl());
		     sr.setId(web.getId().toString());
			 rlist.add(sr);
		}
		model.setTotal(1);
        model.setCurrentPage(1);
        model.setPage(1);
        model.setData(rlist);
        return model;
	}
	
}

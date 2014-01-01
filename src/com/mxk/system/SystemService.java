package com.mxk.system;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mxk.crawler.model.ResourceType;
import com.mxk.crawler.model.SortableComparator;
import com.mxk.crawler.model.Tag;
/**
 * 
 * @author Administrator
 *
 */
@Service
public class SystemService {

	@Autowired
	private MongoOperations mog; 
	
	/**
	 * 保存标签
	 * @param tag
	 */
	public void saveTag(Tag tag){
		mog.save(tag);
	}
	
	public void saveSubTag(){
		
	}
	
	/**
	 * 查询标签
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> findTagsByType(ResourceType type){
		Query q = new Query(Criteria.where("type").is(type.getCode()));
		q.sort().on("sort", Order.DESCENDING); //
		List<Tag> list = mog.find(q, Tag.class);
		if(!list.isEmpty()){
			for(Tag tag : list){
				SortableComparator comparator = new SortableComparator(); //排序 内嵌文档不建议使用monogdb排序
				Collections.sort(tag.getSubtags(), comparator);
				/**
				 * 使用  db.xxx.aggregate([{
                 *    $match: {'game.gid': 02569}
                 *    },{
                 *    $sort: {$date: -1}
                 *    }])
				 */
			}
		}
		return list;
	}
}

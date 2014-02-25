package com.mxk.web.user;

import com.mxk.model.UserCollect;
import com.mxk.web.base.PlusAble;

public class UserCollectPlus extends UserCollect implements PlusAble{

	@SuppressWarnings("unchecked")
	@Override
	public UserCollectPlus copy(Object object) {
		if(object != null && object instanceof UserCollect){
			UserCollect obj = (UserCollect) object;
			this.setColletTarget(obj.getColletTarget());
			this.setColletTargetType(obj.getColletTargetType());
			this.setCreateTime(obj.getCreateTime());
			this.setId(obj.getId());
			this.setSimpleDesc(obj.getSimpleDesc());
			this.setTag(obj.getTag());
			this.setUserId(obj.getUserId());
		}
		return this;
	}

}

package com.mxk.web.user;

import com.mxk.model.UserSubjectPart;
import com.mxk.web.base.PlusAble;

public class UserSubjectPartPlus extends UserSubjectPart implements PlusAble {
    
	private boolean change;//是否修改封面
	
	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	@Override
	public void copy(Object object) {
		if(object != null && object instanceof UserSubjectPart){
			UserSubjectPart o = (UserSubjectPart) object;
			this.setCreateTime(o.getCreateTime());
			this.setId(o.getId());
			this.setImgUrl(o.getImgUrl());
			this.setPartInfo(o.getPartInfo());
			this.setSubjectId(o.getSubjectId());
			this.setSubjectName(o.getSubjectName());
			this.setSubjectType(o.getSubjectType());
			this.setUserId(o.getUserId());
		}
	}
	
}

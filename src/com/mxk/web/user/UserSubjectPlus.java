package com.mxk.web.user;

import com.mxk.model.UserSubject;
import com.mxk.web.base.PlusAble;

public class UserSubjectPlus extends UserSubject implements PlusAble{


	@SuppressWarnings("unchecked")
	@Override
	public UserSubjectPlus copy(Object object) {
		if(object != null && object instanceof UserSubject){
			UserSubject userSubject = (UserSubject) object;
			this.setCategory(userSubject.getCategory());
			this.setCreatetime(userSubject.getCreatetime());
			this.setFaceimage(userSubject.getFaceimage());
			this.setId(userSubject.getId());
			this.setTag(userSubject.getTag());
			this.setTitle(userSubject.getTitle());
			this.setUserid(userSubject.getUserid());
		}
		return this;
	}
	
}

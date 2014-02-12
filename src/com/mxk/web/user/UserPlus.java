package com.mxk.web.user;

import com.mxk.model.User;

public class UserPlus extends User {

	/**
	 * 登录成功凭证 
	 */
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public UserPlus(){
		
	}
	
    public UserPlus(User user){
    	if(user != null){
    		this.setId(user.getId());
    		this.setUserEmail(user.getUserEmail());
    		this.setUserImage(user.getUserImage());
    		this.setUserName(user.getUserName());
    		this.setUserPassword(user.getUserPassword());	
    	}
	}
	
	
}

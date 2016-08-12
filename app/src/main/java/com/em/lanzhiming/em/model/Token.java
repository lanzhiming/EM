package com.em.lanzhiming.em.model;

public class Token extends IdEntity{
	
	/**
	 * 
	 */
	
	
	public Long userId;
	public String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}

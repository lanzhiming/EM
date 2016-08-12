package com.em.lanzhiming.em.model;

public class Task extends IdEntity {

	private String title;
	private String description;
	private User user;

	// JSR303 BeanValidator的校验规则
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

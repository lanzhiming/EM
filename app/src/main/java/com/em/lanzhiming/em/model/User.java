package com.em.lanzhiming.em.model;

import java.util.Date;

public class User extends IdEntity {
	
	/**
	 * 默认的图片地址
	 */
	public static final String DEFAULT_IMG="http://hotusm.oss-cn-beijing.aliyuncs.com/userfile/4ded3a5d-27bd-4c20-be54-8f32f293c4f3.jpg";
	
	private String loginName;
	private String name;
	private String plainPassword;
	private String password;
	private String salt;
	
	
	/**
	 * 角色
	 */
	private String roles;
	/**
	 * 注册时间
	 */
	private Date registerDate;
	/**
	 * 砖石->等级
	 */
	private int integral;
	/**
	 * 登录标示  0代表可以登录  1代表不可以登录
	 */
	private int loginFlag;
	/**
	 * 头像路径
	 */
	private  String imgPath;
	/**
	 * 职位
	 */
	private String position;
	
	/**
	 * 计数器
	 */
	public int counter;
	
	
	public User() {
	}

	public User(String name, int integral, String imgPath, String position) {
		this.name = name;
		this.integral = integral;
		this.imgPath = imgPath;
		this.position = position;
	}

	public User(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public String getRoles() {
		return roles;
	}

	
	public void setRoles(String roles) {
		this.roles = roles;
	}


	// 设定JSON序列化时的日期格式
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}
	
	public int getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(int loginFlag) {
		this.loginFlag = loginFlag;
	}
	
	
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public String toString() {
		return "User{" +
				"loginName='" + loginName + '\'' +
				", name='" + name + '\'' +
				", plainPassword='" + plainPassword + '\'' +
				", password='" + password + '\'' +
				", salt='" + salt + '\'' +
				", roles='" + roles + '\'' +
				", registerDate=" + registerDate +
				", integral=" + integral +
				", loginFlag=" + loginFlag +
				", imgPath='" + imgPath + '\'' +
				", position='" + position + '\'' +
				", counter=" + counter +
				'}';
	}
}
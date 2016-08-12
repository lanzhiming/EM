package com.em.lanzhiming.em.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 *投票记录表 
 *
 */
public class Vote implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Long id;
	/**
	 * 投票人
	 */
	private Long recorderId;
	/**
	 * 投票人
	 */
	private User recorder;
	
	/**
	 * 被投票人
	 */
	private Long reveiverId;
	
	/**
	 * 被投票人
	 */
	private User reveiver;
	
	
	/**
	 * 投票时间
	 * 
	 */
	private Date createDate;
	/**
	 * 描述信息
	 */
	private String descMsg;
	/**
	 * 砖石的登记  1小  2中  3大
	 */
	private int level;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getRecorderId() {
		return recorderId;
	}

	public void setRecorderId(Long recorderId) {
		this.recorderId = recorderId;
	}

	public Long getReveiverId() {
		return reveiverId;
	}

	public void setReveiverId(Long reveiverId) {
		this.reveiverId = reveiverId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescMsg() {
		return descMsg;
	}

	public void setDescMsg(String descMsg) {
		this.descMsg = descMsg;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public User getRecorder() {
		return recorder;
	}

	public void setRecorder(User recorder) {
		this.recorder = recorder;
	}
	
	public User getReveiver() {
		return reveiver;
	}

	public void setReveiver(User reveiver) {
		this.reveiver = reveiver;
	}
	
}

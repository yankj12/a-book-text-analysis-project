package com.yan.novel.schema;

import java.io.Serializable;
import java.util.Date;

public class Author implements Serializable{

	private static final long serialVersionUID = 1L;

	// 主键
	private Integer id;

	// 姓名
	private String name;
	
	// 简介
	private String summary;
	
	// 时期
	private String period;
	
	// 数据来源
	private String dataSource;
	
	// 有效状态
	private String validStatus;
	
	// 插入时间
	private Date insertTime;
	
	// 修改时间
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}

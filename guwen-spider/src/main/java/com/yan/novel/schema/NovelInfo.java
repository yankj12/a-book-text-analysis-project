package com.yan.novel.schema;

import java.io.Serializable;
import java.util.Date;

public class NovelInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String novelUrlToken;
	
	private String novelUrl;
	
	// 小说名称
	private String novelName;
	
	// 作者
	private String authorName;
	
	// 网站最后更新时间
	private String lastUpdateTime;
	
	// 网站最后更新章节名称
	private String lastUpdateChapterFullName;
	
	// 简介
	private String novelSummary;

	private Date insertTime;
	
	private Date updateTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNovelUrlToken() {
		return novelUrlToken;
	}

	public void setNovelUrlToken(String novelUrlToken) {
		this.novelUrlToken = novelUrlToken;
	}

	public String getNovelName() {
		return novelName;
	}

	public void setNovelName(String novelName) {
		this.novelName = novelName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdateChapterFullName() {
		return lastUpdateChapterFullName;
	}

	public void setLastUpdateChapterFullName(String lastUpdateChapterFullName) {
		this.lastUpdateChapterFullName = lastUpdateChapterFullName;
	}

	public String getNovelSummary() {
		return novelSummary;
	}

	public void setNovelSummary(String novelSummary) {
		this.novelSummary = novelSummary;
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

	public String getNovelUrl() {
		return novelUrl;
	}

	public void setNovelUrl(String novelUrl) {
		this.novelUrl = novelUrl;
	}
	
}

package com.yan.novel.schema;

import java.io.Serializable;

public class NovelInfo implements Serializable{

	private String novelUrlToken;
	
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
	
}

package com.yan.novel.spider.service.facade;

import java.util.List;

import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;

public interface NovelCrawlService {
	
	/**
	 * 爬取小说内容
	 * 
	 * @param webRootUrl
	 * @param novelUrlToken
	 * @return
	 * @throws Exception
	 */
	public String crawlNovel(String webRootUrl, String novelUrlToken) throws Exception;
	
	/**
	 * 爬取并保存章节内容
	 * 保存到数据库
	 * 是否保存到文件取决于配置文件中的开关
	 * （这个方法中的代码跟具体的小说网站没有关系）
	 * 
	 * @param novelUrlToken
	 * @param novelInfo
	 * @param novelChapters
	 * @throws Exception 
	 */
	public void crawlAndSaveNovelChapters(String novelUrlToken, NovelInfo novelInfo, List<NovelChapter> novelChapters) throws Exception;
	
}

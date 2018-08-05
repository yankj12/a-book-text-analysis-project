package com.yan.novel.spider.service.facade;

public interface NovelCrawlService {

	/**
	 * 初始化类的参数
	 */
	public void init();
	
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
	 * 爬取章节内容
	 * @param chapterUrl
	 * @return
	 */
	public String crawNovelChapter(String chapterUrl);
	
	/**
	 * 通过get的方式请求一个url
	 * @param url
	 * @return
	 */
	public String requestUrlByGetMethod(String url);
	
	
}

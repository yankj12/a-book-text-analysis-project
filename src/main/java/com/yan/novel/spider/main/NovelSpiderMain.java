package com.yan.novel.spider.main;

import com.yan.novel.spider.service.facade.NovelCrawlService;
import com.yan.novel.spider.service.impl.NovelCrawlServiceBiQuGeImpl;

public class NovelSpiderMain {

	public static void main(String[] args) {
		crawlNovelFromBiQuGe("1_1145");
	}

	/**
	 * 根据小说的urlTken从笔趣阁爬取小说
	 * 
	 * @param novelUrlToken
	 */
	public static void crawlNovelFromBiQuGe(String novelUrlToken) {
		// http://www.biquge.com.tw/2_2144/
		
		String webRootUrl = "http://www.biquge.com.tw";
		
		try {
			NovelCrawlService novelCrawlService = new NovelCrawlServiceBiQuGeImpl();
			novelCrawlService.crawlNovel(webRootUrl, novelUrlToken);
			
			// http://www.biquge.com.tw/2_2144/1268254.html
//			novelCrawlService.crawNovelChapter("http://www.biquge.com.tw/2_2144/1268254.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

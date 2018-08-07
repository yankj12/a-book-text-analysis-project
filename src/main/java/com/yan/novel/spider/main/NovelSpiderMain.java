package com.yan.novel.spider.main;

import com.yan.novel.spider.service.facade.NovelCrawlService;
import com.yan.novel.spider.service.impl.NovelCrawlServiceBXWXImpl;
import com.yan.novel.spider.service.impl.NovelCrawlServiceBiQuGeImpl;
import com.yan.novel.spider.service.impl.NovelCrawlServiceYBDUImpl;

public class NovelSpiderMain {

	public static void main(String[] args) {
		//crawlNovelFromBiQuGe("1_1145");
		
		//crawlNovelFromBXWX("b/13/13370");
		
		// xiaoshuo/10/10316
		// xiaoshuo/4/4631
		// xiaoshuo/10/10644
		// xiaoshuo/6/6433
		crawlNovelFromYBDU("xiaoshuo/6/6433");
	}

	/**
	 * 根据小说的urlTken从笔趣阁爬取小说
	 * 
	 * @param novelUrlToken
	 */
	public static void crawlNovelFromBiQuGe(String novelUrlToken) {
		// http://www.biquge.com.tw/2_2144/
		
		String webRootUrl = "http://www.biquge.com.tw";

		// 爬取数据是否正常结束
		boolean crawlEndWithSuccess = false;
		
		// 只有爬取正常结束才从循环跳出
		// 使用这种机制来处理爬取过程中因异常而中断的情况
		while(!crawlEndWithSuccess){		
			try {
				NovelCrawlService novelCrawlService = new NovelCrawlServiceBiQuGeImpl();
				novelCrawlService.crawlNovel(webRootUrl, novelUrlToken);
				
				// 如果上面的执行是以异常中断的话，应该通过循环继续处理，直到上面的方法正常结束，此时应该跳出循环
				crawlEndWithSuccess = true;
			} catch (Exception e) {
				crawlEndWithSuccess = false;
				e.printStackTrace();
			}
		}
	}
	
	public static void crawlNovelFromBXWX(String novelUrlToken) {
		
		String webRootUrl = "https://www.bxwx.la";
		
		// 爬取数据是否正常结束
		boolean crawlEndWithSuccess = false;
		
		// 只有爬取正常结束才从循环跳出
		// 使用这种机制来处理爬取过程中因异常而中断的情况
		while(!crawlEndWithSuccess){
			try {
				NovelCrawlService novelCrawlService = new NovelCrawlServiceBXWXImpl();
				novelCrawlService.crawlNovel(webRootUrl, novelUrlToken);
				
				// 如果上面的执行是以异常中断的话，应该通过循环继续处理，直到上面的方法正常结束，此时应该跳出循环
				crawlEndWithSuccess = true;
			} catch (Exception e) {
				crawlEndWithSuccess = false;
				e.printStackTrace();
			}
		}
	}

	public static void crawlNovelFromYBDU(String novelUrlToken) {
		
		String webRootUrl = "https://www.ybdu.com";
		
		// 爬取数据是否正常结束
		boolean crawlEndWithSuccess = false;
		
		// 只有爬取正常结束才从循环跳出
		// 使用这种机制来处理爬取过程中因异常而中断的情况
		while(!crawlEndWithSuccess){
			try {
				NovelCrawlService novelCrawlService = new NovelCrawlServiceYBDUImpl();
				novelCrawlService.crawlNovel(webRootUrl, novelUrlToken);
				
				// 如果上面的执行是以异常中断的话，应该通过循环继续处理，直到上面的方法正常结束，此时应该跳出循环
				crawlEndWithSuccess = true;
				
			} catch (Exception e) {
				crawlEndWithSuccess = false;
				e.printStackTrace();
			}
			
		}
	}
}

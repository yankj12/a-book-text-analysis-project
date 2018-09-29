package com.yan.novel.spider.main;

import com.yan.novel.spider.service.facade.NovelCrawlService;
import com.yan.novel.spider.service.impl.BookCrawlServiceGuShiWenImpl;

public class GuShiWenMain {

	public static void main(String[] args) {
		// guwen/book_60.aspx
		crawlBookFromGuShiWen("guwen/book_60.aspx");
	}

	
	public static void crawlBookFromGuShiWen(String bookUrlToken) {
		
		String webRootUrl = "https://so.gushiwen.org";
		
		// 爬取数据是否正常结束
		boolean crawlEndWithSuccess = false;
		
		// 只有爬取正常结束才从循环跳出
		// 使用这种机制来处理爬取过程中因异常而中断的情况
		while(!crawlEndWithSuccess){
			try {
				NovelCrawlService novelCrawlService = new BookCrawlServiceGuShiWenImpl();
				novelCrawlService.crawlNovel(webRootUrl, bookUrlToken);
				
				// 如果上面的执行是以异常中断的话，应该通过循环继续处理，直到上面的方法正常结束，此时应该跳出循环
				crawlEndWithSuccess = true;
				
			} catch (Exception e) {
				crawlEndWithSuccess = false;
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 爬取书籍信息
	 */
	public static void crawlAllBookInfo(){
		// https://so.gushiwen.org/guwen/
		
	}

}

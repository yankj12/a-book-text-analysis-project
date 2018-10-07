package com.yan.novel.spider.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.yan.novel.schema.NovelInfo;
import com.yan.novel.service.facade.NovelInfoDaoService;
import com.yan.novel.service.impl.NovelInfoDaoServiceSpringImpl;
import com.yan.novel.spider.service.facade.NovelCrawlService;
import com.yan.novel.spider.service.impl.AbstrateNovelCrawlServiceImpl;
import com.yan.novel.spider.service.impl.BookCrawlServiceGuShiWenImpl;
import com.yan.novel.util.NovelToFilesUtil;

public class GuShiWenMain {

	public static void main(String[] args) {
		// guwen/book_60.aspx
		//crawlBookFromGuShiWen("guwen/book_60.aspx");
		// 遍历书籍信息
//		crawlAllBookInfo();
		
		// 爬取未下载的书籍，并更新下载标志位
		crawlUnDownloadedBooks();
	}
	
	/**
	 * 爬取未下载的书籍，并更新下载标志位
	 */
	public static void crawlUnDownloadedBooks() {
		// 查询出未下载的书籍
		List<NovelInfo> undownloadedBooks = findUnDownloadedBooks();
		
		// 循环下载书籍
		if(undownloadedBooks != null) {
			int index = 1;
			for(NovelInfo novelInfo:undownloadedBooks) {
				System.out.println(index + "/" + undownloadedBooks.size() + "," + novelInfo.getNovelName());
				// 根据novelUrlToken去爬取书籍
				crawlBookFromGuShiWen(novelInfo.getNovelUrlToken());
				
				index++;
			}
		}
	}
	
	
	public static List<NovelInfo> findUnDownloadedBooks(){
		NovelCrawlService novelCrawlService = new BookCrawlServiceGuShiWenImpl();
		// 未下载的书籍信息列表
		List<NovelInfo> undownloadedBooks = novelCrawlService.findUnDownloadedNovels();
		
		return undownloadedBooks;
	}
	
	// 根据novelUrlToken去爬取书籍
	public static void crawlBookFromGuShiWen(String bookUrlToken) {
		
		String webRootUrl = "https://so.gushiwen.org";
		
		NovelCrawlService novelCrawlService = new BookCrawlServiceGuShiWenImpl();
		
		// 爬取数据是否正常结束
		boolean crawlEndWithSuccess = false;
		
		// 只有爬取正常结束才从循环跳出
		// 使用这种机制来处理爬取过程中因异常而中断的情况
		while(!crawlEndWithSuccess){
			try {
				novelCrawlService.crawlNovel(webRootUrl, bookUrlToken);
				
				// 如果上面的执行是以异常中断的话，应该通过循环继续处理，直到上面的方法正常结束，此时应该跳出循环
				crawlEndWithSuccess = true;
				
			} catch (Exception e) {
				crawlEndWithSuccess = false;
				e.printStackTrace();
			}
			
		}
		
		// 将书籍的downloadFlag置为1
		novelCrawlService.updateNovelInfoDownloadFlag(bookUrlToken);
		
	}
		
	/**
	 * 爬取书籍信息
	 */
	public static void crawlAllBookInfo(){
		// https://so.gushiwen.org/guwen/default.aspx?p=1
		AbstrateNovelCrawlServiceImpl novelCrawlService = new BookCrawlServiceGuShiWenImpl();
		
		// 书籍信息列表
		List<NovelInfo> bookInfos = new ArrayList<NovelInfo>();
		
		int totalPage = 1;
		
		// 页码
		int pageNo = 1;
		
		while(pageNo <= totalPage){
			String webRootUrl = "https://so.gushiwen.org";
			String url = webRootUrl + "/guwen/default.aspx?p=" + pageNo;
			
			// 请求这个url，获取返回的html内容
			String content = novelCrawlService.requestUrlByGetMethod(url);
			
			
			// 使用jsoup解析html内容
			Document document=Jsoup.parse(content);
			
			// 获取总页数
			Element sumPageElement = document.select("label#sumPage").first();
			totalPage = Integer.parseInt(sumPageElement.text());
			
			// 获取书籍信息
			Element element = document.select("div.main3").first();
			
			List<Element> bookElements = element.select("div.left > div.sonspic");
			
			if(bookElements != null){
				for(Element bookElement:bookElements){
					List<Element> pElements = bookElement.select("div.cont > p");
					Element novelNameElement = pElements.get(0);
					Element summaryElement = pElements.get(1);
					
					NovelInfo novelInfo = new NovelInfo();
					
					// 标题
					String novelName = novelNameElement.text();
					System.out.println(pageNo + "/" + totalPage + "," + novelName);
					
					novelInfo.setNovelName(novelName);
					// 简介
					String novelSummary = summaryElement.text();
					novelInfo.setNovelSummary(novelSummary);
					
					Element bookUrlElement = novelNameElement.select("a").first();
					String bookUrlToken = bookUrlElement.attr("href");
					if(bookUrlToken != null 
							&& !"".equals(bookUrlToken.trim())
							&& bookUrlToken.startsWith("/")) {
						bookUrlToken = bookUrlToken.substring(1);
					}
					novelInfo.setNovelUrlToken(bookUrlToken);
					
					// 处理相对链接
					String bookUrl = "";
					String bookRelativeUrl = bookUrlToken;
					if(bookRelativeUrl != null 
							&& !"".equals(bookRelativeUrl.trim()) 
							&& !"#".equals(bookRelativeUrl.trim())){
						if(bookRelativeUrl.startsWith("/")) {
							
						}else {
							bookRelativeUrl = "/" + bookRelativeUrl;
						}
						
						bookUrl = webRootUrl + bookRelativeUrl;
					}
					novelInfo.setNovelUrl(bookUrl);
					novelInfo.setDownloadFlag("0");
					novelInfo.setInsertTime(new Date());
					novelInfo.setUpdateTime(new Date());
					
					bookInfos.add(novelInfo);
				}
			}
			
			// 页数加1
			pageNo++;
		}
		
		// 保存书籍信息
		NovelInfoDaoService novelInfoDaoService = new NovelInfoDaoServiceSpringImpl();
		novelInfoDaoService.insertBathNovelInfo(bookInfos);
		
	}

}

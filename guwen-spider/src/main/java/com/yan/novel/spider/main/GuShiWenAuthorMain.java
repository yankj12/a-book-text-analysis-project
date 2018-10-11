package com.yan.novel.spider.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.yan.novel.schema.Author;
import com.yan.novel.service.facade.AuthorDaoService;
import com.yan.novel.service.impl.AuthorDaoServiceSpringImpl;
import com.yan.novel.spider.service.impl.AbstrateNovelCrawlServiceImpl;
import com.yan.novel.spider.service.impl.BookCrawlServiceGuShiWenImpl;

public class GuShiWenAuthorMain {

	public static void main(String[] args) {
		crawlAuthor();
	}

	
	/**
	 * 爬取书籍信息
	 */
	public static void crawlAuthor(){
		// p表示页数
		// c表示朝代
		// https://so.gushiwen.org/authors/Default.aspx?p=1&c=%e5%85%88%e7%a7%a6

		// 朝代
		String[] periods = new String[]{"先秦", "两汉", "魏晋", "南北朝", "隋代", "唐代", "五代", "宋代", "金朝", "元代", "明代", "清代"};
		
		
		
		for(String period:periods){
			System.out.println(period);
			crawlAuthorByPeriod(period);
		}
		
	}
	
	/**
	 * 爬取某个朝代下的作者
	 * @param period
	 */
	public static void crawlAuthorByPeriod(String period){
		AbstrateNovelCrawlServiceImpl novelCrawlService = new BookCrawlServiceGuShiWenImpl();
		
		// 书籍信息列表
		List<Author> authors = new ArrayList<Author>();
		
		int totalPage = 1;
		
		// 页码
		int pageNo = 1;
		
		while(pageNo <= totalPage){
			String webRootUrl = "https://so.gushiwen.org";
			String url = webRootUrl + "/authors/Default.aspx?p=" + pageNo + "&c=" + period;
			
			// 请求这个url，获取返回的html内容
			String content = novelCrawlService.requestUrlByGetMethod(url);
			
			// 使用jsoup解析html内容
			Document document=Jsoup.parse(content);
			
			// 获取总页数
			Element sumPageElement = document.select("label#sumPage").first();
			totalPage = Integer.parseInt(sumPageElement.text());
			
			List<Element> authorElements = document.select("div.left > div.sonspic");
			
			if(authorElements != null){
				for(Element authorElement:authorElements){
					List<Element> pElements = authorElement.select("div.cont > p");
					Element nameElement = pElements.get(0);
					Element summaryElement = pElements.get(1);
					
					Author author = new Author();
					
					// 标题
					String name = nameElement.text();
					System.out.println(pageNo + "/" + totalPage + "," + name);
					
					author.setName(name);
					// 简介
					String summary = summaryElement.text();
					author.setSummary(summary);
					
					author.setPeriod(period);
					author.setDataSource("gushiwen.org");
					author.setValidStatus("1");
					author.setInsertTime(new Date());
					author.setUpdateTime(new Date());
					
					authors.add(author);
				}
			}
			
			// 页数加1
			pageNo++;
		}
		
		// 保存作者
		AuthorDaoService authorDaoService = new AuthorDaoServiceSpringImpl();
		authorDaoService.insertBathAuthor(authors);
		
	}
	
}

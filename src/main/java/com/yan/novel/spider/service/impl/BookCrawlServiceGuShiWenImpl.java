package com.yan.novel.spider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.service.facade.NovelInfoDaoService;
import com.yan.novel.service.impl.NovelInfoDaoServiceSpringImpl;
import com.yan.novel.spider.service.facade.NovelCrawlService;
import com.yan.novel.util.NovelToFilesUtil;

/**
 * 笔下文学的实现类
 * 
 * @author Yan
 *
 */
public class BookCrawlServiceGuShiWenImpl extends AbstrateNovelCrawlServiceImpl implements NovelCrawlService{

	public String crawlNovel(String webRootUrl, String bookUrlToken) throws Exception{
		String bookUrl = webRootUrl + "/" + bookUrlToken + "/";

		// 请求这个url，获取返回的html内容
		String content = this.requestUrlByGetMethod(bookUrl);
		
		NovelInfo novelInfo = new NovelInfo();
		novelInfo.setNovelUrlToken(bookUrlToken);
		novelInfo.setNovelUrl(bookUrl);
		
		// 使用jsoup解析html内容
		Document document=Jsoup.parse(content);
		Element element = document.select("div.main3").first();
		
		Element leftElement = element.select("div.left").first();
		
		Element contElement = leftElement.select("div.cont").first();
		Element novelNameElement = contElement.select("h1").first();
		Element summaryElement = contElement.select("p").first();
		
		// 标题
		String novelName = novelNameElement.text();
		novelInfo.setNovelName(novelName);
		// 简介
		String novelSummary = summaryElement.text();
		novelInfo.setNovelSummary(novelSummary);
		
		// 首先应该初始化文件夹
		if(this.isWriteToLocal()) {
			//System.out.println("初始化小说文件夹:" + novelInfo.getNovelName());
			NovelToFilesUtil.initLocalDirsAndFiles(this.getWorkRootDirName(), novelName);
		}
		
		// 小说信息写入到本地文件
		if(this.isWriteToLocal()) {
			//System.out.println("小说信息写入到文件:" + novelInfo.getNovelName());
			NovelToFilesUtil.writeNovelInfoToLocalFile(novelInfo, this.getWorkRootDirName());
		}
		
		List<NovelChapter> novelChapters = new ArrayList<NovelChapter>();
		// 使用jsoup获取章节链接
		
		Element sonsElement = leftElement.select("div.sons").first();
		
		List<Element> volumeDivElements = sonsElement.select("div.bookcont");
		if(volumeDivElements != null){
			// 卷序号
			int volumeSerialNo = 1;
			// 章节序号
			int serialNo = 1;
			for(Element volumeElement:volumeDivElements){
				List<Element> elements = volumeElement.children();
				Element volumeNameElement = elements.get(0);
				Element volumeContentElement = elements.get(1);
				
				// 卷名
				String volumeName = volumeNameElement.text();
				
				List<Element> chapterLinkElementList = volumeContentElement.select("a");
				if(chapterLinkElementList != null) {
					for(Element linkElement:chapterLinkElementList) {
						String linkText = linkElement.html();
						String[] ary1 = linkText.split("\\s+");
//						System.out.println(Arrays.toString(ary));
						//  /2_2144/1268254.html
						String chapterRelativeUrl = linkElement.attr("href");
						
						// 处理相对链接
						String chapterUrl = "";
						if(chapterRelativeUrl != null 
								&& !"".equals(chapterRelativeUrl.trim()) 
								&& !"#".equals(chapterRelativeUrl.trim())){
							if(chapterRelativeUrl.startsWith("/")) {
								
							}else {
								chapterRelativeUrl = "/" + chapterRelativeUrl;
							}
							
							chapterUrl = webRootUrl + chapterRelativeUrl;
						}
//						System.out.println(chapterRelativeUrl);
						
						// 从章节相对链接中截取章节的urlToken
						String chapterUrlToken = chapterRelativeUrl;
						
						NovelChapter chapter = new NovelChapter();
						chapter.setNovelUrlToken(bookUrlToken);
						
						chapter.setVolumeSerialNo(volumeSerialNo);
						chapter.setVolumeName(volumeName);
						
						chapter.setSerialNo(serialNo);
						chapter.setChapterFullName(linkText);
						chapter.setChapterUrlToken(chapterUrlToken);
						chapter.setChapterUrl(chapterUrl);
						if(ary1 != null && ary1.length >= 2){
							chapter.setChapterSerialName(ary1[0].trim());
							chapter.setChapterName(ary1[1].trim());
						}else if(ary1 != null && ary1.length >= 1){
							chapter.setChapterName(ary1[0].trim());
						}
						
						novelChapters.add(chapter);
						serialNo++;
						
						System.out.println("获取章节链接:" + chapter.getChapterFullName());
					}
				}
				
				volumeSerialNo++;
			}
		}

		NovelInfoDaoService novelInfoDaoService = new NovelInfoDaoServiceSpringImpl();
				NovelInfo novelInfoTmp = novelInfoDaoService.queryNovelInfoByNovelUrlToken(bookUrlToken);
		if(novelInfoTmp != null) {
			
		}else {
			novelInfoDaoService.insertNovelInfo(novelInfo);
			
		}
		
		// 爬取并保存章节内容，保存到数据库和文件
		this.crawlAndSaveNovelChapters(bookUrlToken, novelInfoTmp, novelChapters);
		
		System.out.println("保存结束");

		return null;
	}
	
	@Override
	public String requestUrlByGetMethod(String url) {
		Map<String, String> requestHeaders = new HashMap<>();
		
		requestHeaders.put("Host", "so.gushiwen.org");
		requestHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
		requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		requestHeaders.put("Accept-Encoding", "gzip, deflate, br");
		requestHeaders.put("Cookie", "Hm_lvt_04660099568f561a75456483228a9516=1538030719,1538030787,1538030855,1538191650; Hm_lpvt_04660099568f561a75456483228a9516=1538195604; ASP.NET_SessionId=guitrl5q1afuibud5kgjjbfc");
		requestHeaders.put("Connection", "keep-alive");
		requestHeaders.put("Upgrade-Insecure-Requests", "1");
		requestHeaders.put("Pragma", "no-cache");
		requestHeaders.put("Cache-Control", "no-cache");
		
		String html = this.requestUrlByGetMethod(url, requestHeaders, "UTF-8");
		return html;
	}

	/**
	 * 爬取章节内容
	 * @param chapterUrl
	 * @return
	 */
	public String crawNovelChapter(String chapterUrl) {
		if(chapterUrl == null || "".equals(chapterUrl.trim())){
			return "";
		}
		
		StringBuilder article = new StringBuilder();
		
		// 不需要对html进行非空判断，因为如果为空的话，后面解析会报错，但是你需要判断下为什么会报错
		String html = this.requestUrlByGetMethod(chapterUrl);
		
		// 解析html
		// div id=content
		Document document=Jsoup.parse(html);
		Element element = document.select("div.main3").first();
		List<Element> divElements = element.children();
		// 古文
		Element articleOldElement = divElements.get(0);
		
		// 获取id
		String divId = articleOldElement.attr("id");
		String id = divId.replaceAll("left", "");
		
		// 古文
		List<Element> articleOldParagraphElements = articleOldElement.select("div.contson > p");
		if(articleOldParagraphElements != null){
			for(Element paragraphElement:articleOldParagraphElements){
				article.append(paragraphElement.text()).append("\n");
			}
		}
		
		// 分割线
		article.append("\n\n--------------------------------------------\n\n");
		
		// 译文
		// 需要调用翻译api
		String url = "https://so.gushiwen.org/guwen/ajaxbfanyi.aspx?id=" + id;
		String htmlNow = this.requestUrlByGetMethod(url);
		Document documentNow = Jsoup.parse(htmlNow);
		
		// 译文
		List<Element> articleNowParagraphElements = documentNow.select("div.shisoncont > p");
		if(articleNowParagraphElements != null){
			for(Element paragraphElement:articleNowParagraphElements){
				article.append(paragraphElement.text()).append("\n");
			}
		}
			
		//System.out.println(chapterContent);
		return article.toString();
	}

}

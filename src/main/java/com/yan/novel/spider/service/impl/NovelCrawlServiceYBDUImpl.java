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
import com.yan.novel.util.NovelHtmlUtil;
import com.yan.novel.util.NovelToFilesUtil;

/**
 * 笔下文学的实现类
 * 
 * @author Yan
 *
 */
public class NovelCrawlServiceYBDUImpl extends AbstrateNovelCrawlServiceImpl implements NovelCrawlService{

	
	public String crawlNovel(String webRootUrl, String novelUrlToken) throws Exception{
		String novelUrl = webRootUrl + "/" + novelUrlToken + "/";

		// 请求这个url，获取返回的html内容
		String content = this.requestUrlByGetMethod(novelUrl);
		
		//TODO
		NovelInfo novelInfo = new NovelInfo();
		novelInfo.setNovelUrlToken(novelUrlToken);
		novelInfo.setNovelUrl(novelUrl);
		
		// 使用jsoup解析html内容
		Document document=Jsoup.parse(content);
		Element element = document.select("div.mu_contain").first();
		Element novelNameElement = element.select("div.mu_h1").first();
		
		// 标题
		String novelName = novelNameElement.text();
		novelInfo.setNovelName(novelName);
		
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
		Element chapterListDivElement = element.select("ul.mulu_list").first();
		List<Element> chapterLinkElementList = chapterListDivElement.select("a");
		if(chapterLinkElementList != null) {
			int serialNo = 1;
			for(Element linkElement:chapterLinkElementList) {
				String linkText = linkElement.html();
				String[] ary1 = linkText.split("\\s+");
//				System.out.println(Arrays.toString(ary));
				//  /2_2144/1268254.html
				String chapterRelativeUrl = linkElement.attr("href");
				
				// 处理相对链接
				// 相对链接有如下两种情况
				// 7739239.html 需要拼接成 /b/13/13370/7739239.html
				// /b/13/13370/7290870.html 不需要处理
				// 实际上就是相对链接中间要加上urlToken
				if(chapterRelativeUrl.startsWith("/")) {
					if(chapterRelativeUrl.startsWith("/" + novelUrlToken)) {
						
					}else {
						chapterRelativeUrl = "/" + novelUrlToken + chapterRelativeUrl;
					}
				}else {
					if(chapterRelativeUrl.startsWith(novelUrlToken)) {
						chapterRelativeUrl = "/" + chapterRelativeUrl;
					}else {
						chapterRelativeUrl = "/" + novelUrlToken + "/"+ chapterRelativeUrl;
					}
				}
				
				String chapterUrl = webRootUrl + chapterRelativeUrl;
//				System.out.println(chapterRelativeUrl);
				
				// 从章节相对链接中截取章节的urlToken
				int index = chapterRelativeUrl.lastIndexOf("/");
				int index2 = chapterRelativeUrl.lastIndexOf(".html");
				String chapterUrlToken = chapterRelativeUrl.substring(index+1, index2);
				
				NovelChapter chapter = new NovelChapter();
				chapter.setNovelUrlToken(novelUrlToken);
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

		NovelInfoDaoService novelInfoDaoService = new NovelInfoDaoServiceSpringImpl();
				NovelInfo novelInfoTmp = novelInfoDaoService.queryNovelInfoByNovelUrlToken(novelUrlToken);
		if(novelInfoTmp != null) {
			
		}else {
			novelInfoDaoService.insertNovelInfo(novelInfo);
			
		}
		
		// 爬取并保存章节内容，保存到数据库和文件
		this.crawlAndSaveNovelChapters(novelUrlToken, novelInfoTmp, novelChapters);
		
		System.out.println("保存结束");

		return null;
	}
	
	@Override
	public String requestUrlByGetMethod(String url) {
		Map<String, String> requestHeaders = new HashMap<>();
		
		requestHeaders.put("Host", "www.ybdu.com");
		requestHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");
		requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		requestHeaders.put("Accept-Encoding", "gzip, deflate, br");
		requestHeaders.put("Cookie", "Hm_lvt_a1b17bf301c6a7aa3a0ba9cce1414f7a=1533356555,1533484333,1533562179; cscpvrich8519_fidx=7; Hm_lpvt_a1b17bf301c6a7aa3a0ba9cce1414f7a=1533562179");
		requestHeaders.put("Connection", "keep-alive");
		requestHeaders.put("Upgrade-Insecure-Requests", "1");
		requestHeaders.put("Pragma", "no-cache");
		requestHeaders.put("Cache-Control", "no-cache");
		
		String html = this.requestUrlByGetMethod(url, requestHeaders, "gbk");
		return html;
	}

	/**
	 * 爬取章节内容
	 * @param chapterUrl
	 * @return
	 */
	public String crawNovelChapter(String chapterUrl) {
		
		String html = this.requestUrlByGetMethod(chapterUrl);
		
		// 解析html
		// div id=content
		Document document=Jsoup.parse(html);
		Element element = document.select("div#htmlContent").first();
		String chapterContent = element.html();
		//System.out.println(chapterContent);
		
		// 去掉章节内容中的html标签
		chapterContent = NovelHtmlUtil.removeHtmlTags(chapterContent);
		
		return chapterContent;
	}

}

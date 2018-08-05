package com.yan.novel.spider.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.yan.common.util.PropertiesIOUtil;
import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.service.facade.NovelChapterDaoService;
import com.yan.novel.service.facade.NovelInfoDaoService;
import com.yan.novel.service.impl.NovelChapterDaoServiceSpringImpl;
import com.yan.novel.service.impl.NovelInfoDaoServiceSpringImpl;
import com.yan.novel.spider.service.facade.NovelCrawlService;
import com.yan.novel.util.NovelHtmlUtil;
import com.yan.novel.util.NovelToFilesUtil;

public class NovelCrawlServiceBiQuGeImpl implements NovelCrawlService{

	// 是否使用代理
	private boolean useProxy = false;
	private String proxyIp = null;
	private int port = 0;
	
	// 是否写入到本地文件
	private boolean writeToLocal;
	// 小说内容持久化到本地文件夹中的位置
	private String workRootDirName;
	
	/**
	 * 初始化类的参数
	 */
	public void init() {
		try {
			Properties properties = PropertiesIOUtil.loadProperties("/config.properties");
			this.useProxy = Boolean.parseBoolean(properties.getProperty("useProxy"));
			if(useProxy){
				this.proxyIp = properties.getProperty("proxy.ip");
				this.port = Integer.parseInt(properties.getProperty("proxy.port"));
			}
			
			// 是否写入到本地文件
			this.writeToLocal = Boolean.parseBoolean(properties.getProperty("writeToLocal"));
			if(writeToLocal) {
				// 小说内容持久化到本地文件
				this.workRootDirName = properties.getProperty("rootDir");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public NovelCrawlServiceBiQuGeImpl(){
		// 初始化参数
		this.init();
	}
	
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
		Element element = document.select("div#maininfo").first();
//		for(Element childElement : element.children()) {
//			System.out.println("#############################");
//			System.out.println(childElement.wholeText());
//		}
		// 小说标题、作者、最后更新时间、最后更新章节
		Element infoElement = element.select("div#info").first();
		// 标题
		String novelName = infoElement.child(0).text();
		novelInfo.setNovelName(novelName);
		
		// 作者
		String authorName = infoElement.child(1).text();
		String[] ary = authorName.split("[:：]");
		authorName = (ary != null && ary.length>=2?ary[1]:authorName);
		novelInfo.setAuthorName(authorName);
		
		// 最后更新
		String lastUpdateTime = infoElement.child(3).text();
		ary = lastUpdateTime.split("[:：]");
		lastUpdateTime = (ary != null && ary.length>=2?ary[1]:lastUpdateTime);
		novelInfo.setLastUpdateTime(lastUpdateTime);
		
		// 最新章节
		String lastUpdateChapterFullName = infoElement.child(4).text();
		ary = lastUpdateChapterFullName.split("[:：]");
		lastUpdateChapterFullName = (ary != null && ary.length>=2?ary[1]:lastUpdateChapterFullName);
		novelInfo.setLastUpdateChapterFullName(lastUpdateChapterFullName);
		
		// 小说简介信息
		Element introElement = element.select("div#intro").first();
		String novelSummary = introElement.text();
		novelInfo.setNovelSummary(novelSummary);
		
		// 首先应该初始化文件夹
		if(this.writeToLocal) {
			//System.out.println("初始化小说文件夹:" + novelInfo.getNovelName());
			NovelToFilesUtil.initLocalDirsAndFiles(this.workRootDirName, novelName);
		}
		
		// 小说信息写入到本地文件
		if(this.writeToLocal) {
			//System.out.println("小说信息写入到文件:" + novelInfo.getNovelName());
			NovelToFilesUtil.writeNovelInfoToLocalFile(novelInfo, this.workRootDirName);
		}
		
		List<NovelChapter> novelChapters = new ArrayList<NovelChapter>();
		// 使用jsoup获取章节链接
		Element chapterListDivElement = document.select("div#list").first();
		List<Element> chapterLinkElementList = chapterListDivElement.select("a");
		if(chapterLinkElementList != null) {
			int serialNo = 1;
			for(Element linkElement:chapterLinkElementList) {
				String linkText = linkElement.html();
				String[] ary1 = linkText.split("\\s+");
//				System.out.println(Arrays.toString(ary));
				//  /2_2144/1268254.html
				String chapterRelativeUrl = linkElement.attr("href");
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
		
		NovelChapterDaoService novelChapterDaoService = new NovelChapterDaoServiceSpringImpl();
		
		
		// 章节链接举例
		// 绝对路径    http://www.biquge.com.tw/2_2144/1268254.html
		// 相对路径    /2_2144/1268254.html
		
		int chapterCount = 1;
		List<NovelChapter> novelChaptersForSave = new ArrayList<>();
		if(novelChapters != null){
			for(NovelChapter novelChapter : novelChapters){
				System.out.println("请求章节内容:" + novelChapter.getChapterFullName());
				
				String novelChapterUrl = novelChapter.getChapterUrl();
				String chapterContent = this.crawNovelChapter(novelChapterUrl);
				novelChapter.setChapterContent(chapterContent);
				
				novelChapter.setInsertTime(new Date());
				novelChapter.setUpdateTime(new Date());
				
				novelChaptersForSave.add(novelChapter);
				// 将内容写入到文件中
				// 章节内容写入到本地文件
				if(this.writeToLocal) {
					//System.out.println("章节内容写入到文件:" + novelChapter.getChapterFullName());
					NovelToFilesUtil.writeNovelChapterToLocalFile(novelInfo, novelChapter, this.workRootDirName);
				}
				
				// 每100条保存一次
				if(chapterCount % 100 == 0) {
					
					// 数据库保存时判断重复并去重
					// 去重策略：
					// 1、查下数据库，存在的章节跳过处理步骤
					// 2、根据序号的启示和终止，进行删除操作，再插入
					// 3、进行批量update操作（不过判断逻辑稍微复杂些）
					int fromSerialNo = novelChaptersForSave.get(0).getSerialNo();
					int toSerialNo = novelChaptersForSave.get(novelChaptersForSave.size()-1).getSerialNo();
					
					Map<String, Object> map = new HashMap<>();
					map.put("novelUrlToken", novelUrlToken);
					map.put("fromSerialNo", fromSerialNo);
					map.put("toSerialNo", toSerialNo);
					
					// 先查一下在序号区间内的章节数目
					int countOfChaptersInSerialNoRegion = novelChapterDaoService.countNovelChaptersBySerialNoRegion(map);
					if(countOfChaptersInSerialNoRegion > 0) {
						// 如果在序号区间内的章节数目大于0的话，删除序号区间内的章节，便于后续插入
						
						System.out.println("序号区间[" + fromSerialNo + "-" + toSerialNo + "]已存在章节数[" + countOfChaptersInSerialNoRegion + "]，先删后插");
						novelChapterDaoService.deleteNovelChaptersBySerialNoRegion(map);
					}
					
					System.out.println("每100条保存一次");
					novelChapterDaoService.insertBathNovelChapter(novelChaptersForSave);
					// 清空list
					novelChaptersForSave.clear();
				}
				chapterCount++;
			}
		}
		
		// 如果集合中还有元素，保存一次
		if(novelChaptersForSave.size() > 0) {
			novelChapterDaoService.insertBathNovelChapter(novelChaptersForSave);
			// 清空list
			novelChaptersForSave.clear();
		}
		System.out.println("保存结束");

		return null;
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
		Element element = document.select("div#content").first();
		String chapterContent = element.html();
		//System.out.println(chapterContent);
		
		// 去掉章节内容中的html标签
		chapterContent = NovelHtmlUtil.removeHtmlTags(chapterContent);
		
		return chapterContent;
	}
	
	/**
	 * 通过get的方式请求一个url
	 * @param url
	 * @return
	 */
	public String requestUrlByGetMethod(String url) {
		CloseableHttpClient httpclient = null;
		
        //实例化CloseableHttpClient对象
        if(this.useProxy){
        	// 使用代理服务器
        	
        	//设置代理IP、端口、协议（请分别替换）
        	HttpHost proxy = new HttpHost(proxyIp, port, "http");
        	
        	//把代理设置到请求配置
        	RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
        	httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        }else{
        	// 不使用代理服务器
        	httpclient = HttpClients.createDefault();
        }
        
		HttpGet httpget = new HttpGet(url);
		
		httpget.addHeader("Host", "www.biquge.com.tw");
		httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");
		httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		httpget.addHeader("Accept-Encoding", "gzip, deflate");
		httpget.addHeader("Referer", "https://www.baidu.com/link?url=RJ7MpkvYpENEfeXkw7-VTDKT39DFECqQvmQYvHsJLNr9dSfdCxuOQR7_646eQs9a&wd=&eqid=ea3343c700047d65000000025b5ac858");
		httpget.addHeader("Cookie", "__cdnuid=c5d84123c59bad6d9f3437f188427588");
		httpget.addHeader("Connection", "keep-alive");
		httpget.addHeader("Upgrade-Insecure-Requests", "1");
		httpget.addHeader("If-Modified-Since", "Wed, 25 Jul 2018 07:17:07 GMT");
		httpget.addHeader("If-None-Match", "80dba87ee723d41:0");
		httpget.addHeader("Cache-Control", "max-age=0");
		
		String content = null;
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
//		        InputStream instream = entity.getContent();
//		        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
//		        try {
//		        	String line = null;
//		            while((line = br.readLine()) != null){
//		            	System.out.println(line);
//		            }
//		        } finally {
//		            instream.close();
//		        }
				content = EntityUtils.toString(entity, "gbk");
//                System.out.println("Response content: " + content);
                
		    }
			
			// 请求最小间隔3s
//			Thread.sleep(3 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		    try {
				if(response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}
	
}

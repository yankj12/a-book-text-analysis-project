package com.yan.novel.spider.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.yan.common.util.PropertiesIOUtil;
import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.service.facade.NovelChapterDaoService;
import com.yan.novel.service.facade.NovelInfoDaoService;
import com.yan.novel.service.impl.NovelChapterDaoServiceSpringImpl;
import com.yan.novel.service.impl.NovelInfoDaoServiceSpringImpl;
import com.yan.novel.util.NovelToFilesUtil;

public abstract class AbstrateNovelCrawlServiceImpl{

	// 是否使用代理
	// 私有属性可以被集成，但无法被访问，需要提供get和set方法
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
	
	public AbstrateNovelCrawlServiceImpl(){
		// 初始化参数
		this.init();
	}
	
	
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
	public void crawlAndSaveNovelChapters(String novelUrlToken, NovelInfo novelInfo, List<NovelChapter> novelChapters) throws Exception {
		NovelChapterDaoService novelChapterDaoService = new NovelChapterDaoServiceSpringImpl();
		
		// 章节链接举例
		// 绝对路径    http://www.biquge.com.tw/2_2144/1268254.html
		// 相对路径    /2_2144/1268254.html
		
		List<NovelChapter> novelChaptersForSave = new ArrayList<>();
		if(novelChapters != null){
			for(int i=0;i<novelChapters.size();i++){
				NovelChapter novelChapter = novelChapters.get(i);
				
				int chapterCount = i + 1;
				
				int serialNo = novelChapter.getSerialNo();
				
				Map<String, Object> condition = new HashMap<>();
				condition.put("novelUrlToken", novelUrlToken);
				condition.put("fromSerialNo", serialNo);
				condition.put("toSerialNo", serialNo+99);
				
				int count = novelChapterDaoService.countNovelChaptersBySerialNoRegion(condition);
				if(count == 100) {
					i += 99;
					System.out.println("序号区间[" + serialNo + "-" + (serialNo+99) + "]章节已保存，跳过");
					continue;
				}
				
				System.out.println("请求章节内容:" + novelChapter.getChapterFullName());
				
				String novelChapterUrl = novelChapter.getChapterUrl();
				if(novelChapterUrl != null && !"".equals(novelChapterUrl.trim())){
					try {
						// try-catch ，避免请求html及解析过程中报错导致后续流程不走
						// 需要同时爬取章节的内容和章节名称。因为有些网站章节名称是截取固定长度的
						NovelChapter chapterTmp = this.crawNovelChapter(novelChapterUrl);
						if(chapterTmp != null){
							if(chapterTmp.getChapterContent() != null && !"".equals(chapterTmp.getChapterContent())){
								novelChapter.setChapterContent(chapterTmp.getChapterContent());
							}
							
							if(chapterTmp.getChapterFullName() != null && !"".equals(chapterTmp.getChapterFullName())){
								novelChapter.setChapterFullName(chapterTmp.getChapterFullName());
							}
							
							if(chapterTmp.getChapterName() != null && !"".equals(chapterTmp.getChapterName())){
								novelChapter.setChapterName(chapterTmp.getChapterName());
							}
							
							if(chapterTmp.getChapterSerialName() != null && !"".equals(chapterTmp.getChapterSerialName())){
								novelChapter.setChapterSerialName(chapterTmp.getChapterSerialName());
							}
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
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
		// 保存结束
	}
	
	/**
	 * 爬取章节内容
	 * （这个方法中的代码跟具体的小说网站有很大关系，一方面是请求头header，另一方面是对返回html的处理）
	 * 
	 * @param chapterUrl
	 * @return
	 */
	public abstract NovelChapter crawNovelChapter(String chapterUrl);
	
	/**
	 * 通过get的方式请求一个url
	 * @param url
	 * @return
	 */
	public abstract String requestUrlByGetMethod(String url);
	
	/**
	 * 通过get的方式请求一个url
	 * @param url
	 * @param requestHeaders
	 * @param charset
	 * @return
	 */
	public String requestUrlByGetMethod(String url, Map<String, String> requestHeaders, String charset) {
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
		
		if(requestHeaders != null && requestHeaders.size() > 0) {
			for(Entry<String, String> entry:requestHeaders.entrySet()) {
				httpget.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		String content = null;
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				content = EntityUtils.toString(entity, charset);
                
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

	public List<NovelInfo> findUnDownloadedNovels() {
		NovelInfoDaoService novelInfoDaoService = new NovelInfoDaoServiceSpringImpl();
		Map<String, Object> condition = new HashMap<>();
		condition.put("downloadFlag", "0");
		List<NovelInfo> novelInfos = novelInfoDaoService.queryNovelInfosByCondition(condition);
		
		return novelInfos;
	}

	public void updateNovelInfoDownloadFlag(String novelUrlToken, String downloadFlag) {
		NovelInfoDaoService novelInfoDaoService = new NovelInfoDaoServiceSpringImpl();
		NovelInfo novelInfo = new NovelInfo();
		novelInfo.setNovelUrlToken(novelUrlToken);
		novelInfo.setDownloadFlag(downloadFlag);
		novelInfo.setUpdateTime(new Date());
		
		novelInfoDaoService.updateDownloadFlagByNovelUrlToken(novelInfo);
		
	}

	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isWriteToLocal() {
		return writeToLocal;
	}

	public void setWriteToLocal(boolean writeToLocal) {
		this.writeToLocal = writeToLocal;
	}

	public String getWorkRootDirName() {
		return workRootDirName;
	}

	public void setWorkRootDirName(String workRootDirName) {
		this.workRootDirName = workRootDirName;
	}
	
}

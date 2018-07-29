package com.yan.novel.spider.service;

import java.util.Arrays;
import java.util.List;
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
import com.yan.common.util.io.FileUtil;

/**
 * 
 * 使用httpclient处理请求
 * 
 * 使用jsoup解析html
 * https://www.cnblogs.com/zerotomax/p/7246815.html
 */
public class NovelCrawlService {

	// 是否使用代理
	private boolean useProxy = false;
	private String proxyIp = null;
	private int port = 0;
	
	public NovelCrawlService(){
		try {
			Properties properties = PropertiesIOUtil.loadProperties("/config.properties");
			this.useProxy = Boolean.parseBoolean(properties.getProperty("useProxy"));
			if(useProxy){
				this.proxyIp = properties.getProperty("proxy.ip");
				this.port = Integer.parseInt(properties.getProperty("proxy.port"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String crawlNovel(String url) throws Exception{
		
//		request header
//		
//		Host: www.biquge.com.tw
//		User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0
//		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//		Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2
//		Accept-Encoding: gzip, deflate
//		Referer: https://www.baidu.com/link?url=RJ7MpkvYpENEfeXkw7-VTDKT39DFECqQvmQYvHsJLNr9dSfdCxuOQR7_646eQs9a&wd=&eqid=ea3343c700047d65000000025b5ac858
//		Cookie: __cdnuid=c5d84123c59bad6d9f3437f188427588
//		Connection: keep-alive
//		Upgrade-Insecure-Requests: 1
//		If-Modified-Since: Wed, 25 Jul 2018 07:17:07 GMT
//		If-None-Match: "80dba87ee723d41:0"
//		Cache-Control: max-age=0
		
		CloseableHttpClient httpclient = null;
		
        //实例化CloseableHttpClient对象
        if(useProxy){
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
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
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
                System.out.println("Response content: " + content);
                FileUtil.writeToFile("C:\\Users\\Yan\\Desktop\\novel.html", content, "UTF-8");
		    }
			
		} finally {
		    response.close();
		}
		
		// 使用jsoup解析html内容
		Document document=Jsoup.parse(content);
		Element element = document.select("div#maininfo").first();
		for(Element childElement : element.children()) {
			System.out.println("#############################");
			System.out.println(childElement.wholeText());
		}
		
		// 使用jsoup获取章节链接
		Element chapterListDivElement = document.select("div#list").first();
		List<Element> chapterLinkElementList = chapterListDivElement.select("a");
		for(Element linkElement:chapterLinkElementList) {
			String linkText = linkElement.html();
			String[] ary = linkText.split("\\s+");
			System.out.println(Arrays.toString(ary));
			System.out.println(linkElement.attr("href"));
		}
		
		// 章节链接举例
		// 绝对路径    http://www.biquge.com.tw/2_2144/1268254.html
		// 相对路径    /2_2144/1268254.html
		
		return null;
	}
	
	
	public static void main(String[] args) {
		// http://www.biquge.com.tw/2_2144/
		
		String url = "http://www.biquge.com.tw/2_2144/";
		try {
			new NovelCrawlService().crawlNovel(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}

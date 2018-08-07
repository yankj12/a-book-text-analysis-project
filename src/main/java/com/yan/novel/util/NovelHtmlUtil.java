package com.yan.novel.util;

public class NovelHtmlUtil {

	/**
	 * 移除内容中的html标签
	 * @param htmlContent
	 * @return
	 */
	public static String removeHtmlTags(String htmlContent){
		
		// html标签
		// 简单标签处理
		htmlContent = htmlContent.replace("<br>", "\n");
		htmlContent = htmlContent.replace("<br/>", "\n");
		
		// 去掉<br/>这类<xxx/>的标签
		// 从<的下标开始找>和/>的下标，比较两者的下标来判断是否为<xxx/>这类标签
		
		// 复杂标签处理
		// 比如：
		// 标签中间包含着代码、中文等
		// 标签中间包含着链接，链接标签中上一页、下一页、返回目录等
		// 处理逻辑：
		// 如果标签中间不包含中文，去掉整个标签
		// 如果标签中间包含中文，但是中文只是上一页、下一页、返回目录等，去掉整个标签
		
		
		// html中的不可见字符
		htmlContent = htmlContent.replace("&nbsp;", " ");
		
		// html中的转义字符
		
		return htmlContent;
	}
	
	/**
	 * 移除掉文本中的大段html代码块
	 * @param content
	 * @return
	 */
	public static String removeHtmlCodeBlock(String content){
		// 识别出html标签
		int htmlTagStartIndex = content.indexOf("<");
		int htmlTagEndIndex = content.indexOf(">");
		
		// 处理后的最终结果
		StringBuilder resultBuilder = new StringBuilder();
		String preString = content.substring(0, htmlTagStartIndex);
		resultBuilder.append(preString);
		
		String suffixString = content.substring(htmlTagStartIndex);
		System.out.println(suffixString);
		
		htmlTagStartIndex = suffixString.indexOf("<");
		htmlTagEndIndex = suffixString.indexOf(">");
		
		// 有些html标签中不仅包含标签名，还包含样式等信息
		// 有些标签是<xxx />这种的
		String htmlTagFull = suffixString.substring(htmlTagStartIndex+1, htmlTagEndIndex);
		
		String[] tagStrs = htmlTagFull.split("\\s+");
		String htmlTag = tagStrs[0];
		
		int startTagIndex = suffixString.indexOf("<" + htmlTag);
		int endTagIndex = suffixString.indexOf("</" + htmlTag);
		
		int endTagEndIndex = suffixString.substring(endTagIndex).indexOf(">");
		
		String htmlText = suffixString.substring(startTagIndex, endTagIndex + endTagEndIndex + 1);
		System.out.println(htmlText);
		
		return content;
	}
	
}

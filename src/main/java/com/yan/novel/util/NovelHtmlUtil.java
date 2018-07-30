package com.yan.novel.util;

public class NovelHtmlUtil {

	/**
	 * 移除内容中的html标签
	 * @param htmlContent
	 * @return
	 */
	public static String removeHtmlTags(String htmlContent){
		
		// html标签
		htmlContent = htmlContent.replace("<br>", "\n");
		
		// html中的不可见字符
		htmlContent = htmlContent.replace("&nbsp;", " ");
		
		// html中的转义字符
		
		return htmlContent;
	}
	
}

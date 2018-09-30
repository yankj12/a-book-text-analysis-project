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
		htmlContent = htmlContent.replaceAll("<br>", "\n");
		htmlContent = htmlContent.replaceAll("<br/>", "\n");
		
		// 去掉<br/>这类<xxx/>的标签
		// 从<的下标开始找>和/>的下标，比较两者的下标来判断是否为<xxx/>这类标签
		
		// 复杂标签处理
		// 比如：
		// 标签中间包含着代码、中文等
		// 标签中间包含着链接，链接标签中上一页、下一页、返回目录等
		// 处理逻辑：
		// 如果标签中间不包含中文，去掉整个标签
		// 如果标签中间包含中文，但是中文只是上一页、下一页、返回目录等，去掉整个标签
		htmlContent = removeHtmlCodeBlock(htmlContent);
		
		// html中的不可见字符
		htmlContent = htmlContent.replaceAll("&nbsp;", " ");
		
		// html中的转义字符
		
		return htmlContent;
	}
	
	/**
	 * 移除掉文本中的大段html代码块
	 * 可以处理<xxx />这种情况
	 * 但是不处理<xxx >这种情况
	 * 
	 * @param content
	 * @return
	 */
	public static String removeHtmlCodeBlock(String content){
		if(content != null) {
			StringBuilder builder = new StringBuilder(content);
			
			// 跟着游标处理
			for(int i=0;i<builder.length();i++) {
				// 从游标起开始搜索html标签
				int htmlTagStartIndex = builder.indexOf("<", i);
				int htmlTagEndIndex = builder.indexOf(">", i);
				
				// 没有<或者>字符的时候，肯定就没有html标签了，应该退出了
				if(htmlTagStartIndex < 0 || htmlTagEndIndex < 0 || htmlTagEndIndex <= htmlTagStartIndex) {
					break;
				}
				
				// 有些html标签中不仅包含标签名，还包含样式等信息
				// 有些标签是<xxx />这种的
				String htmlTagFull = builder.substring(htmlTagStartIndex+1, Math.max(htmlTagEndIndex, i));
				
				String[] tagStrs = htmlTagFull.split("\\s+");
				String htmlTag = tagStrs[0];
				
				int startTagIndex = builder.indexOf("<" + htmlTag, i);
				int endTagIndex = builder.indexOf("</" + htmlTag, Math.max(startTagIndex, i));
				
				// <xxx > 这类的不处理
				int endTagEndIndex = i;
				if(endTagIndex > 0) {
					// <xxx >标签对应</xxx>的结尾标签
					endTagEndIndex = builder.indexOf(">", endTagIndex);
				}else {
					// <xxx >没有</xxx>的结尾标签的情况
					// 找 /> 并且 />和<之间（大于<的下标，小于/>的下标）没有其他的<或者>标签。如果没有找到符合的/>标签，那么就是<xxx >的情况，应该跳出本轮循环，并且置下标i
					int tmpIndex = builder.indexOf("/>", Math.max(startTagIndex, i));
					if( tmpIndex > 0 
							&& !builder.substring(Math.max(startTagIndex, i)+1, tmpIndex).contains(">")
							&& !builder.substring(Math.max(startTagIndex, i)+1, tmpIndex).contains("<")) {
						// 符合<xxx />的情况
						// 因为/>是两个字符，所以下标需要加1，表示>所在的下标
						endTagEndIndex = tmpIndex + 1;
					}else {
						// 符合<xxx >的这种情况，下标i应该置为 > 的下标
						i = builder.indexOf(">", Math.max(startTagIndex, i));
						continue;
					}
				}
				
				String htmlText = builder.substring(startTagIndex, endTagEndIndex + 1);
				//System.out.println(htmlText);
				
				// 找不到html代码块，应该退出循环了
				if(htmlText == null || "".equals(htmlText)) {
					break;
				}
				// 此处需要保留非html的字符串
				builder.replace(startTagIndex, endTagEndIndex + 1, "");
			}
			
			return builder.toString();
		}else {
			return null;
		}
	}

}

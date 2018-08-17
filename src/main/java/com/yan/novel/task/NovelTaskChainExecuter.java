package com.yan.novel.task;

import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.task.schema.NovelTaskChain;
import com.yan.novel.task.schema.noveltask.CreateIndexNovelTask;
import com.yan.novel.task.schema.noveltask.RemoveChapterHtmlCodeNovelTask;
import com.yan.novel.task.schema.noveltask.WriteChapterToTextFileNovelTask;

public class NovelTaskChainExecuter {

	public static void main(String[] args) {
		NovelTaskChain novelTaskChain = new NovelTaskChain();
		
		// 将任务添加到任务链中
		novelTaskChain.addTask(new RemoveChapterHtmlCodeNovelTask("移除html代码块"));
		novelTaskChain.addTask(new WriteChapterToTextFileNovelTask("将小说章节写入文件"));
		novelTaskChain.addTask(new CreateIndexNovelTask("创建索引"));
		
		
		NovelChapter novelChapter = null;
		NovelInfo novelInfo = null;
		
		// 执行责任链
		novelTaskChain.doNovelTask(novelChapter, novelInfo);
		
	}

}

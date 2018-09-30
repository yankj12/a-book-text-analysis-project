package com.yan.novel.task.schema.noveltask;

import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.task.schema.AbstractNovelTask;

public class RemoveChapterHtmlCodeNovelTask extends AbstractNovelTask{

	public RemoveChapterHtmlCodeNovelTask(String taskName) {
		super(taskName);
	}

	@Override
	protected void doWork(NovelChapter novelChapter, NovelInfo novelInfo) {
		// TODO Auto-generated method stub
		System.out.println("移除html代码块");
		
	}

}

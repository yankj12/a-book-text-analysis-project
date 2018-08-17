package com.yan.novel.task.schema.noveltask;

import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.task.schema.AbstractNovelTask;

public class WriteChapterToTextFileNovelTask extends AbstractNovelTask{

	public WriteChapterToTextFileNovelTask(String taskName) {
		super(taskName);
	}

	@Override
	protected void doWork(NovelChapter novelChapter, NovelInfo novelInfo) {
		// TODO Auto-generated method stub
		System.out.println("将小说章节写入文件");
		
	}

}

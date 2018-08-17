package com.yan.novel.task.schema.noveltask;

import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.task.schema.AbstractNovelTask;

public class CreateIndexNovelTask extends AbstractNovelTask{

	public CreateIndexNovelTask(String taskName) {
		super(taskName);
	}

	@Override
	protected void doWork(NovelChapter novelChapter, NovelInfo novelInfo) {
		// TODO Auto-generated method stub
		System.out.println("创建索引");
		
	}

}

package com.yan.novel.task.schema;

import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;

public class NovelTaskChain {

	// 责任链中头元素
	private AbstractNovelTask headNovelTask;
	
	// 责任链中尾元素
	private AbstractNovelTask lastNovelTask;
	
	public NovelTaskChain addTask(AbstractNovelTask novelTask){
		if(this.headNovelTask == null){
			this.headNovelTask = novelTask;
		}
		
		if(this.lastNovelTask == null){
			this.lastNovelTask = novelTask;
		}else{
			this.lastNovelTask.nextNovelTask = novelTask;
			this.lastNovelTask = novelTask;
		}
		
		return this;
	}
	
	public void doNovelTask(NovelChapter novelChapter, NovelInfo novelInfo){
		if(headNovelTask!= null){
			headNovelTask.doTask(novelChapter, novelInfo);
		}
	}
	
}

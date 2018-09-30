package com.yan.novel.task.schema;

import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;

public abstract class AbstractNovelTask {

	protected String taskName;
	
	// 责任链中下一个元素
	public AbstractNovelTask nextNovelTask;
	
	public AbstractNovelTask(String taskName){
		this.taskName = taskName;
	};
	
	public void setNextNovelTask(AbstractNovelTask nextNovelTask){
		this.nextNovelTask = nextNovelTask;
	}
	
	/**
	 * 通过责任链调用任务
	 * @param novelChapter
	 * @param novelInfo
	 */
	public void doTask(NovelChapter novelChapter, NovelInfo novelInfo){
		System.out.println("[" + taskName + "]的任务开始");
		// 执行本元素的任务
		this.doWork(novelChapter, novelInfo);
		System.out.println("[" + taskName + "]的任务结束");
		
		// 调用责任链中下一个元素
		if(this.nextNovelTask != null){
			nextNovelTask.doTask(novelChapter, novelInfo);
		}
		
	}
	
	/**
	 * 每个NovelTask需要实现的处理任务
	 * （只能类内部调用自己的doWork方法，不能通过外部直接调用此方法）
	 * 
	 * @param novelChapter
	 * @param novelInfo
	 */
	protected abstract void doWork(NovelChapter novelChapter, NovelInfo novelInfo);
	
}
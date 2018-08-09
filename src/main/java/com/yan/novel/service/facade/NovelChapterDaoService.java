package com.yan.novel.service.facade;

import java.util.List;
import java.util.Map;

import com.yan.novel.schema.NovelChapter;

public interface NovelChapterDaoService {

public final static String MAPPER_NAME_SPACE ="com.yan.novel.mapping.NovelChapterMapper";

	boolean insertNovelChapter(NovelChapter novelChapter);
	
	boolean insertBathNovelChapter(List<NovelChapter> novelChapters);
	
	NovelChapter queryNovelChapterByNovelUrlTokenAndChapterUrlToken(String novelUrlToken, String chapterUrlToken);
	
	List<NovelChapter> queryNovelChaptersByNovelUrlToken(String novelUrlToken);

	/**
	 * 统计章节序号区间内的章节数目
	 * @param map
	 * @return
	 */
	int countNovelChaptersBySerialNoRegion(Map<String, Object> map);
	
	/**
	 * 删除章节序号区间内的章节（包含首尾）
	 * @param map
	 */
	void deleteNovelChaptersBySerialNoRegion(Map<String, Object> map);
	
	/**
	 * 更新章节内容字段
	 * @param novelChapter
	 */
	void updateChapterContentByNovelUrlTokenAndChapterUrlToken(NovelChapter novelChapter);
	
	/**
	 * 批量更新章节内容
	 * @param novelChapters
	 */
	void updateChapterContentBath(List<NovelChapter> novelChapters);
}

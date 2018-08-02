package com.yan.novel.service.facade;

import java.util.List;

import com.yan.novel.schema.NovelChapter;

public interface NovelChapterDaoService {

public final static String MAPPER_NAME_SPACE ="com.yan.novel.mapping.NovelChapterMapper";

	boolean insertNovelChapter(NovelChapter novelChapter);
	
	boolean insertBathNovelChapter(List<NovelChapter> novelChapters);
	
	NovelChapter queryNovelChapterByNovelUrlTokenAndChapterUrlToken(String novelUrlToken, String chapterUrlToken);
	
	List<NovelChapter> queryNovelChaptersByNovelUrlToken(String novelUrlToken);
}

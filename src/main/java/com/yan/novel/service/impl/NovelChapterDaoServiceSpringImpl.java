package com.yan.novel.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yan.common.util.jdbc.JdbcUtil;
import com.yan.novel.schema.NovelChapter;
import com.yan.novel.service.facade.NovelChapterDaoService;


public class NovelChapterDaoServiceSpringImpl implements NovelChapterDaoService{


	@Override
	public boolean insertNovelChapter(NovelChapter novelChapter) {
		boolean result = false;
		novelChapter.setInsertTime(new Date());
		novelChapter.setUpdateTime(new Date());
		
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "insertNovelChapter";
		sqlSession.insert(statement, novelChapter);
		
		sqlSession.close();
		
		result = true;
		return result;
	}

	
	public NovelChapter queryNovelChapterByNovelUrlToken(String novelUrlToken) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryNovelChapterByNovelUrlToken";
		NovelChapter novelChapter = sqlSession.selectOne(statement, novelUrlToken);
		
		sqlSession.close();
		return novelChapter;
	}


	@Override
	public boolean insertBathNovelChapter(List<NovelChapter> novelChapters) {
		boolean result = false;
		
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "insertBathNovelChapter";
		sqlSession.insert(statement, novelChapters);
		
		sqlSession.close();
		
		result = true;
		return result;
	}
	
}

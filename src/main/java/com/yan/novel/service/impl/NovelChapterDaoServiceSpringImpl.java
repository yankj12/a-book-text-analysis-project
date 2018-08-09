package com.yan.novel.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	
	public NovelChapter queryNovelChapterByNovelUrlTokenAndChapterUrlToken(String novelUrlToken, String chapterUrlToken) {
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("novelUrlToken", novelUrlToken);
		conditions.put("chapterUrlToken", chapterUrlToken);
		
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryNovelChapterByNovelUrlTokenAndChapterUrlToken";
		NovelChapter novelChapter = sqlSession.selectOne(statement, conditions);
		
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


	@Override
	public List<NovelChapter> queryNovelChaptersByNovelUrlToken(String novelUrlToken) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryNovelChaptersByNovelUrlToken";
		List<NovelChapter> novelChapters = sqlSession.selectList(statement, novelUrlToken);
		
		sqlSession.close();
		return novelChapters;
	}


	@Override
	public int countNovelChaptersBySerialNoRegion(Map<String, Object> map) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "countNovelChaptersBySerialNoRegion";
		int count = sqlSession.selectOne(statement, map);
		
		sqlSession.close();
		return count;
	}


	@Override
	public void deleteNovelChaptersBySerialNoRegion(Map<String, Object> map) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "deleteNovelChaptersBySerialNoRegion";
		sqlSession.delete(statement, map);
		
		sqlSession.close();
	}


	@Override
	public void updateChapterContentByNovelUrlTokenAndChapterUrlToken(NovelChapter novelChapter) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "updateChapterContentByNovelUrlTokenAndChapterUrlToken";
		sqlSession.update(statement, novelChapter);
		
		sqlSession.close();
		
	}


	@Override
	public void updateChapterContentBath(List<NovelChapter> novelChapters) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "updateChapterContentBath";
		sqlSession.update(statement, novelChapters);
		
		sqlSession.close();
		
	}
	
}

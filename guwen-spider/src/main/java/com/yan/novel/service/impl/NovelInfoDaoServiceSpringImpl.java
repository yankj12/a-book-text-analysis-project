package com.yan.novel.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.yan.common.util.jdbc.JdbcUtil;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.service.facade.NovelInfoDaoService;


public class NovelInfoDaoServiceSpringImpl implements NovelInfoDaoService{


	@Override
	public boolean insertNovelInfo(NovelInfo novelInfo) {
		boolean result = false;
		novelInfo.setInsertTime(new Date());
		novelInfo.setUpdateTime(new Date());
		
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "insertNovelInfo";
		sqlSession.insert(statement, novelInfo);
		
		sqlSession.close();
		
		result = true;
		return result;
	}

	
	public NovelInfo queryNovelInfoByNovelUrlToken(String novelUrlToken) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryNovelInfoByNovelUrlToken";
		NovelInfo novelInfo = sqlSession.selectOne(statement, novelUrlToken);
		
		sqlSession.close();
		return novelInfo;
	}


	@Override
	public boolean insertBathNovelInfo(List<NovelInfo> novelInfos) {
		boolean result = false;
		
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "insertBathNovelInfo";
		sqlSession.insert(statement, novelInfos);
		
		sqlSession.close();
		
		result = true;
		return result;
	}


	@Override
	public List<NovelInfo> queryNovelInfosByNovelName(String novelName) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryNovelInfosByNovelName";
		List<NovelInfo> novelInfos = sqlSession.selectList(statement, novelName);
		
		sqlSession.close();
		return novelInfos;
	}


	@Override
	public List<NovelInfo> queryNovelInfosByCondition(Map<String, Object> condition) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryNovelInfosByCondition";
		List<NovelInfo> novelInfos = sqlSession.selectList(statement, condition);
		
		sqlSession.close();
		return novelInfos;
	}


	@Override
	public void updateDownloadFlagByNovelUrlToken(NovelInfo novelInfo) {
		novelInfo.setUpdateTime(new Date());
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "updateDownloadFlagByNovelUrlToken";
		sqlSession.update(statement, novelInfo);
		
		sqlSession.close();
	}
	
}

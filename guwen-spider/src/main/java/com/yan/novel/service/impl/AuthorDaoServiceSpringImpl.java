package com.yan.novel.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.yan.common.util.jdbc.JdbcUtil;
import com.yan.novel.schema.Author;
import com.yan.novel.service.facade.AuthorDaoService;


public class AuthorDaoServiceSpringImpl implements AuthorDaoService{

	@Override
	public boolean insertAuthor(Author author) {
		boolean result = false;
		author.setInsertTime(new Date());
		author.setUpdateTime(new Date());
		
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "insertAuthor";
		sqlSession.insert(statement, author);
		
		sqlSession.close();
		
		result = true;
		return result;
	}

	
	public Author queryAuthorById(String novelUrlToken) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryAuthorById";
		Author author = sqlSession.selectOne(statement, novelUrlToken);
		
		sqlSession.close();
		return author;
	}


	@Override
	public boolean insertBathAuthor(List<Author> authors) {
		boolean result = false;
		
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "insertBathAuthor";
		sqlSession.insert(statement, authors);
		
		sqlSession.close();
		
		result = true;
		return result;
	}


	@Override
	public List<Author> queryAuthorsByName(String name) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryAuthorsByName";
		List<Author> authors = sqlSession.selectList(statement, name);
		
		sqlSession.close();
		return authors;
	}


	@Override
	public List<Author> queryAuthorsByCondition(Map<String, Object> condition) {
		SqlSession sqlSession = JdbcUtil.getSqlSession(true);
		
		String statement = MAPPER_NAME_SPACE + "." + "queryAuthorsByCondition";
		List<Author> authors = sqlSession.selectList(statement, condition);
		
		sqlSession.close();
		return authors;
	}

}

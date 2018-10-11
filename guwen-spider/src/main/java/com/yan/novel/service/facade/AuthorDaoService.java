package com.yan.novel.service.facade;

import java.util.List;
import java.util.Map;

import com.yan.novel.schema.Author;

public interface AuthorDaoService {

public final static String MAPPER_NAME_SPACE ="com.yan.novel.mapping.AuthorMapper";

	boolean insertAuthor(Author author);
	
	boolean insertBathAuthor(List<Author> authors);
	
	Author queryAuthorById(String novelUrlToken);
	
	List<Author> queryAuthorsByName(String novelName);
	
	List<Author> queryAuthorsByCondition(Map<String, Object> condition);
	
}

package com.yan.novel.service.facade;

import java.util.List;
import java.util.Map;

import com.yan.novel.schema.NovelInfo;

public interface NovelInfoDaoService {

public final static String MAPPER_NAME_SPACE ="com.yan.novel.mapping.NovelInfoMapper";

	boolean insertNovelInfo(NovelInfo novelInfo);
	
	boolean insertBathNovelInfo(List<NovelInfo> novelInfos);
	
	NovelInfo queryNovelInfoByNovelUrlToken(String novelUrlToken);
	
	List<NovelInfo> queryNovelInfosByNovelName(String novelName);
	
	List<NovelInfo> queryNovelInfosByCondition(Map<String, Object> condition);
	
	void updateDownloadFlagByNovelUrlToken(NovelInfo novelInfo);
}

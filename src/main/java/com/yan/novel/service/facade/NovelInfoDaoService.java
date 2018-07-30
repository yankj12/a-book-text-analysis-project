package com.yan.novel.service.facade;

import java.util.List;

import com.yan.novel.schema.NovelInfo;

public interface NovelInfoDaoService {

public final static String MAPPER_NAME_SPACE ="com.yan.novel.mapping.NovelInfoMapper";

	boolean insertNovelInfo(NovelInfo novelInfo);
	
	boolean insertBathNovelInfo(List<NovelInfo> novelInfos);
	
	NovelInfo queryNovelInfoByNovelUrlToken(String novelUrlToken);
}

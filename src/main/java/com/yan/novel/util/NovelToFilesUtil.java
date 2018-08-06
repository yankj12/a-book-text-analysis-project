package com.yan.novel.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.yan.common.util.io.FileUtil;
import com.yan.novel.schema.NovelChapter;
import com.yan.novel.schema.NovelInfo;
import com.yan.novel.service.facade.NovelChapterDaoService;
import com.yan.novel.service.facade.NovelInfoDaoService;
import com.yan.novel.service.impl.NovelChapterDaoServiceSpringImpl;
import com.yan.novel.service.impl.NovelInfoDaoServiceSpringImpl;

/**
 * 提供将小说从数据库查询出来持久化到文件的工具类
 * 
 * 根据小说名称查询出小说信息和章节信息，持久化到对应的磁盘位置
 * 
 * 文件夹层级如下
 * workRootDir
 * 	|-novelName
 * 		|-NovelInfo.txt
 * 		|-chapters
 *	 		|-chapterSerialNo.txt
 * 
 * @author Yan
 *
 */
public class NovelToFilesUtil {

	public static void main(String[] args) throws Exception {
		String workRootDirName = "E:\\文档\\小说";
		String novelName = "临高启明";
		// 根据小说名称，从数据库读取出内容写入到本地文件系统
		readNovelFromDbAndWriteToLocalFile(workRootDirName, novelName);
		
	}

	/**
	 * 根据小说名称，从数据库读取出内容写入到本地文件系统
	 * @param workRootDirName
	 * @param novelName
	 * @throws Exception
	 */
	public static void readNovelFromDbAndWriteToLocalFile(String workRootDirName, String novelName) throws Exception {
		
		// 初始化相关文件夹层级
		initLocalDirsAndFiles(workRootDirName, novelName);
		
		// 根据小说名称查询
		// 查询NovelInfo, NovelChapter两张表中数据
		// 声明dao类
		NovelInfoDaoService novelInfoDaoService = new NovelInfoDaoServiceSpringImpl();
		NovelChapterDaoService novelChapterDaoService = new NovelChapterDaoServiceSpringImpl();
		List<NovelInfo> novelInfos = novelInfoDaoService.queryNovelInfosByNovelName(novelName);
		
		NovelInfo novelInfo = null;
		if(novelInfos != null && novelInfos.size() > 0) {
			novelInfo = novelInfos.get(0);
			// 将小说信息写入到文件中
			writeNovelInfoToLocalFile(novelInfo, workRootDirName);
		}else {
			// 根据小说名称找不到小说
			System.out.println("根据小说名称找不到小说");
			System.out.println("退出");
			return ;
		}
		
		String novelUrlToken = null;
		if(novelInfo != null) {
			novelUrlToken = novelInfo.getNovelUrlToken();
		}
		
		List<NovelChapter> novelChapters = novelChapterDaoService.queryNovelChaptersByNovelUrlToken(novelUrlToken);
		if(novelChapters != null && novelChapters.size() > 0) {
			for(NovelChapter novelChapter:novelChapters) {
				// 将章节内容写入到文件中
				writeNovelChapterToLocalFile(novelInfo, novelChapter, workRootDirName);
			}

		}
	}
	
	/**
	 * 初始化文件结构
	 * 
	 * @param workRootDirName
	 * @param novelName
	 */
	public static void initLocalDirsAndFiles(String workRootDirName, String novelName) {
		// 判断根目录是否存在，不存在创建根目录
		File workRootDir = new File(workRootDirName);
		if(!workRootDir.exists()) {
			workRootDir.mkdirs();
		}
		
		// 判断小说的文件夹是否存在，不存在则创建
		String novelNameDirName = workRootDirName + "\\" + novelName;
		File novelNameDir = new File(novelNameDirName);
		if(!novelNameDir.exists()) {
			novelNameDir.mkdirs();
		}
		
		// 判断章节的目录是否存在，不存在则创建目录
		String chaptersDirName = novelNameDirName + "\\chapters";
		File chaptersDir = new File(chaptersDirName);
		if(!chaptersDir.exists()) {
			chaptersDir.mkdirs();
		}
	}
	
	/**
	 * 将小说信息写入到文件中
	 * 
	 * @param novelInfo
	 * @param workRootDirName
	 * @throws Exception
	 */
	public static void writeNovelInfoToLocalFile(NovelInfo novelInfo, String workRootDirName) throws Exception {
		
		// 判断小说的文件夹是否存在，不存在则创建
		String novelNameDirName = workRootDirName + "\\" + novelInfo.getNovelName().trim();
		
		// 写入NovelInfo.txt
		String novelInfoTextFileName = novelNameDirName + "\\NovelInfo.txt";
		File novelInfoTextFile = new File(novelInfoTextFileName);
		if(!novelInfoTextFile.exists()) {
			novelInfoTextFile.createNewFile();
		}
		// 写入内容
		// 内容格式
		// novelUrlToken, novelUrl, novelName, authorName, lastUpdateTime, lastUpdateChapterFullName, novelSummary
		StringBuilder novelInfoBuilder = new StringBuilder();
		novelInfoBuilder.append("[").append(novelInfo.getNovelUrlToken()!=null?novelInfo.getNovelUrlToken():"").append("]");
		novelInfoBuilder.append("[").append(novelInfo.getNovelUrl()!=null?novelInfo.getNovelUrl():"").append("]");
		novelInfoBuilder.append("[").append(novelInfo.getNovelName()!=null?novelInfo.getNovelName():"").append("]");
		novelInfoBuilder.append("[").append(novelInfo.getAuthorName()!=null?novelInfo.getAuthorName():"").append("]");
		novelInfoBuilder.append("[").append(novelInfo.getLastUpdateTime()!=null?novelInfo.getLastUpdateTime():"").append("]");
		novelInfoBuilder.append("[").append(novelInfo.getLastUpdateChapterFullName()!=null?novelInfo.getLastUpdateChapterFullName():"").append("]");
		novelInfoBuilder.append("[").append(novelInfo.getNovelSummary()!=null?novelInfo.getNovelSummary():"").append("]");
		// 写入内容
		FileUtil.writeToFile(novelInfoTextFileName, novelInfoBuilder.toString(), "UTF-8");
		
	}
	
	/**
	 * 将章节内容写入到本地文件中
	 * @param novelInfo
	 * @param novelChapter
	 * @param workRootDirName
	 * @throws Exception
	 */
	public static void writeNovelChapterToLocalFile(NovelInfo novelInfo, NovelChapter novelChapter, String workRootDirName) throws Exception {
		// 判断小说的文件夹是否存在，不存在则创建
		String novelNameDirName = workRootDirName + "\\" + novelInfo.getNovelName().trim();
		// 判断章节的目录是否存在，不存在则创建目录
		String chaptersDirName = novelNameDirName + "\\chapters";
		
		// 将内容分章节写入文本文件
		String chapterTextFileName = chaptersDirName + "\\" + novelChapter.getSerialNo() + ".txt";
		File chapterTextFile = new File(chapterTextFileName);
		if(!chapterTextFile.exists()) {
			chapterTextFile.createNewFile();
		}
		// 写入内容
		FileUtil.writeToFile(chapterTextFileName, novelChapter.getChapterContent(), "UTF-8");
	}
}

package com.yan.novel.util;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.yan.common.util.io.FileUtil;

public class NovelIndexUtil {

	/**
	 * 索引位置根目录
	 */
	public static String indexRootDir = "E:\\文档\\luceneIndexRoot\\novelIndex";
	
	/**
	 * 小说根目录
	 */
	public static String novelRootDir = "E:\\文档\\小说";
	
	public static void main(String[] args) throws Exception {
		
		// 清除索引根目录
		cleanIndex();
		
		File novelRoot = new File(novelRootDir);
		// 每部小说的文件夹
		File[] novelDirs = novelRoot.listFiles();
		
		// 遍历每部小说，再遍历每部小说下的所有章节
		for(File novelDir:novelDirs){
			File chapterDir = novelDir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					// 过滤文件夹
					return pathname.isDirectory();
				}
			})[0];
			
			File[] chapterFiles = chapterDir.listFiles();
			for(File chapterFile:chapterFiles){
				createIndex(FileUtil.readFromFile(chapterFile.getAbsolutePath(), "UTF-8"));
			}
		}
	}

	public static void cleanIndex(){
		File indexRoot = new File(indexRootDir);
		File[] files = indexRoot.listFiles();
		for(File file:files){
			file.deleteOnExit();
		}
	}
	
	public static void createIndex(String text) throws Exception{
		
		// 一、创建词法分析器
		Analyzer analyzer = new SmartChineseAnalyzer();

		// 二、创建索引存储目录
		// Store the index in memory:
		//Directory directory = new RAMDirectory();
		// To store an index on disk, use this instead:
		Directory directory = FSDirectory.open(Paths.get(indexRootDir));
		
		// 三、创建索引写入器
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		// 追加的方式添加索引
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		
		IndexWriter iwriter = new IndexWriter(directory, config);
		
		// 四、将内容存储到索引
		Document doc = new Document();
		doc.add(new Field("content", text, TextField.TYPE_STORED));
		iwriter.addDocument(doc);
		iwriter.close();
	}
	
	
}

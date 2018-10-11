# guwen-spider
只是一个用于爬取古诗文的工具

## 计划要做的
- 实现基本的小说爬取
	- 解决重复爬取数据重复的问题
	- 解决增量爬取的问题
- 数据保存
	- 实现数据保存到数据库
	- 构建本地目录结构及文件结构，将数据保存到文件中
		- 设计中间数据的文件夹及文件，便于处理中间数据
	
- 使用lucene建立索引
- 使用结巴中文分词
- 进行词频统计等分析类功能


## 开发设计
### 开发设计概述
数据先存储到文本文件中，后续同步或者异步地存储到数据库

### 数据存储到文件
文件名及文件格式汇总
- 存储小说urlToken、小说名称、小说链接
- 存储章节URLToken、章节序号、章节名称、章节链接
- 存储章节内容

文件夹布局

```
workDir
	|-novelRoot
		|-chapterList.txt
		|-chapters
			|-chapter1.txt
			|-chapter2.txt
```

### 数据存储到数据库
表结构汇总

|表名|表中文名|说明|
|---|---|---|
|NovelInfo|小说基本信息表|保存小说的标题、作者、简介等信息|
|NovelChapter|小说章节内容表|保存小说的每章节的内容|
|Author|作者表|保存作者信息|

#### 作者表Author
|列名|中文名|类型|
|---|---|---|
|id|主键|string|
|name|姓名|string|
|summary|简介|string|
|period|时期|string|
|datasource|数据来源|string|
|validStatus|有效状态|string|
|insertTime|插入时间|date|
|updateTime|修改时间|date|

#### 标签表Tag
|列名|中文名|类型|说明|
|---|---|---|---|
|id|标签id|string|
|tagname|标签名|string|
|category|类别|string|
|masterTagId|主标签id|string|当两个标签判定为重复时，合并为一个|
|validStatus|有效状态|string|
|insertTime|插入时间|date|
|updateTime|修改时间|date|

#### 作者标签对照表AuthorTag
|列名|中文名|类型|说明|
|---|---|---|---|
|id|id|string|
|authorId|作者id|String|
|tagId|标签id|string|
|validStatus|有效状态|string|
|insertTime|插入时间|date|
|updateTime|修改时间|date|

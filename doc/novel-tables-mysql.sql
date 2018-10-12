

CREATE TABLE NovelInfo (
  id BIGINT NOT NULL AUTO_INCREMENT,
  novelUrlToken varchar(20) DEFAULT NULL,
  novelUrl varchar(255) DEFAULT NULL,
  novelName varchar(255) DEFAULT NULL,
  authorName varchar(60) DEFAULT NULL,
  lastUpdateTime varchar(40) DEFAULT NULL,
  lastUpdateChapterFullName varchar(120) DEFAULT NULL,
  novelSummary TEXT DEFAULT NULL,
  insertTime datetime DEFAULT NULL,
  updateTime datetime DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE NovelChapter (
  id BIGINT NOT NULL AUTO_INCREMENT,
  novelUrlToken varchar(20) DEFAULT NULL,
  chapterUrlToken varchar(20) DEFAULT NULL,
  serialNo int DEFAULT 1,
  chapterSerialName varchar(30) DEFAULT NULL,
  chapterName varchar(90) DEFAULT NULL,
  chapterFullName varchar(120) DEFAULT NULL,
  chapterUrl varchar(255) DEFAULT NULL,
  chapterContent LONGTEXT DEFAULT NULL,
  insertTime datetime DEFAULT NULL,
  updateTime datetime DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 卷序号
alter table novelchapter add volumeSerialNo int DEFAULT NULL after chapterUrlToken;
-- 卷名称
alter table novelchapter add volumeName varchar(90) DEFAULT NULL after volumeSerialNo;

ALTER TABLE novelchapter MODIFY COLUMN chapterUrlToken VARCHAR(60);
ALTER TABLE novelchapter CHANGE COLUMN id id BIGINT NOT NULL AUTO_INCREMENT;
-- 修改某列为自增主键
--ALTER TABLE novelchapter CHANGE COLUMN id id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY;

-- 下载标志位，0表示未下载，1表示下载完成。默认为0
alter table NovelInfo add downloadFlag varchar(2) DEFAULT '0' after novelSummary;


CREATE TABLE Author (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name varchar(40) DEFAULT NULL,
  summary TEXT DEFAULT NULL,
  period varchar(40) DEFAULT NULL,
  datasource varchar(20) DEFAULT NULL,
  validStatus varchar(2) DEFAULT '1',
  insertTime datetime DEFAULT NULL,
  updateTime datetime DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 作者代码
alter table Author add authorCode varchar(40) DEFAULT NULL after name;
-- 主作者id，主要用于将多条作者记录合并为一条记录
alter table Author add masterAuthorId BIGINT DEFAULT NULL after datasource;




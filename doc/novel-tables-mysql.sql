

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


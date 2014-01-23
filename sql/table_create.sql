CREATE  TABLE `mxkdatabase`.`TB_WEB_RESOURCE` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(250) NOT NULL COMMENT '标题',
  `image` VARCHAR(250) NOT NULL COMMENT '小图片',
  `images` VARCHAR(2500) COMMENT '三张大图片图片原始网站地址 ,号分隔',
  `ownername` VARCHAR(100) NULL COMMENT '作者名字',
  `sitename` VARCHAR(100) NULL COMMENT '网站名字',
  `siteurl` VARCHAR(100) NULL COMMENT '网站的http链接地址',
  `url` VARCHAR(150) NOT NULL COMMENT '内容对应的http链接地址',
  `info` VARCHAR(4500) NULL COMMENT '简要内容',
  `multiinfo` VARCHAR(250) NULL COMMENT '多样性内容',
  `hits` INT NULL COMMENT '阅读次数',
  `posts` INT NULL COMMENT '评论次数',
  `insignificance` INT NULL COMMENT '用户评论无用',
  `significance` INT NULL COMMENT '用户评论有用',
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `url_UNIQUE` (`url` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '互联网资源';


CREATE  TABLE `mxkdatabase`.`TB_BASE_LINK` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键', 
  `matchUrl` VARCHAR(250) NOT NULL COMMENT '匹配的url',
  `url` VARCHAR(250) NOT NULL COMMENT '链接url地址',
  `describes` VARCHAR(450) COMMENT '链接描述',
  `state` INT NOT NULL COMMENT '链接状态',
  `ctime` datetime COMMENT '创建时间' ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `url_UNIQUE` (`url` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '基础爬取链接地址';



INSERT INTO `mxkdatabase`.`tb_base_link` (`id`, `matchUrl`, `url`, `describe`, `state`, `ctime`) VALUES ('3', 'http://www.moxing.net/bbs/forum', 'http://www.moxing.net/bbs/forum-6-1.html', '', '1', now());

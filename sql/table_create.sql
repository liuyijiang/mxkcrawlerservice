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

CREATE  TABLE `mxkdatabase`.`tb_user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `user_name` VARCHAR(200) NULL ,
  `user_image` VARCHAR(200) NULL ,
  `user_email` VARCHAR(200) NULL ,
  `user_password` VARCHAR(100) NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `user_email_UNIQUE` (`user_email` ASC) ,
  UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '用户基本信息表';

CREATE  TABLE `mxkdatabase`.`tb_user_search_log` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `keyword` VARCHAR(255) NULL ,
  `search_form_ip` VARCHAR(15) NULL ,
  `search_from_user` INT NULL ,
  `create_time` DATETIME NULL ,
  `search_from_site` INT NULL COMMENT '是app 还是web1app 2web' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '记录用户搜索的内容';

CREATE  TABLE `mxkdatabase`.`tb_user_collect` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `user_id` INT NOT NULL ,
  `collet_target` INT NOT NULL COMMENT '收藏目标id' ,
  `collet_target_type` INT NOT NULL COMMENT '收藏目标类型(链接 图片)' ,
  `create_time` DATETIME NOT NULL COMMENT '创建时间' ,
  `tag` VARCHAR(50) NULL ,
  `simple_desc` VARCHAR(100) NOT NULL COMMENT '简单收藏描述' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '用户收藏';


CREATE  TABLE `mxkdatabase`.`tb_subject` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `category` VARCHAR(10) NOT NULL COMMENT '类型 | 原创 分享' ,
  `faceimage` VARCHAR(100) NOT NULL COMMENT '封面图片' ,
  `title` VARCHAR(100) NOT NULL COMMENT '标题' ,
  `tag` VARCHAR(45) NOT NULL COMMENT '类型 舰船 航空' ,
  `userid` INT NOT NULL ,
  `createTime` DATETIME NOT NULL ,
  `show` INT NOT NULL COMMENT '是否在文章首页显示',
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '发布专题表';

CREATE  TABLE `mxkdatabase`.`tb_part` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `subject_id` INT NOT NULL ,
  `user_id` INT NOT NULL ,
  `img_url` VARCHAR(100) NULL ,
  `subject_name` VARCHAR(100) NOT NULL ,
  `subject_type` VARCHAR(10) NOT NULL ,
  `part_info` VARCHAR(255) NULL ,
  `create_time` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '专题部分';


CREATE  TABLE `mxkdatabase`.`tb_catalog_resource` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL COMMENT '名字' ,
  `tag` VARCHAR(100) NULL COMMENT '标签 逗号隔开' ,
  `type` INT NULL COMMENT '军舰 飞机' ,
  `describes` VARCHAR(300) NULL COMMENT '简单描述' ,
  `create_time` DATETIME NOT NULL ,
  `image_url` VARCHAR(100) NULL COMMENT '图片链接' ,
  `hot` INT NULL COMMENT '热度' ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '编目后的资源 与tb_web_resource关联';


INSERT INTO `mxkdatabase`.`tb_base_link` (`matchUrl`, `url`, `describes`, `state`, `ctime`) 
VALUES 
( 'http://www.militarymodelling.com/forums/threads.asp', 'http://www.militarymodelling.com/forums/threads.asp?t=93', 'MilitaryModelling', '1', now()); 

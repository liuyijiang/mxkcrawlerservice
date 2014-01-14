CREATE  TABLE `mxk`.`TB_WEB_RESOURCE` (
  `id` INT NOT NULL ,
  `title` VARCHAR(200) NULL ,
  `image` VARCHAR(250) NULL ,
  `owner` VARCHAR(100) NULL ,
  `sitename` VARCHAR(45) NULL ,
  `siteurl` VARCHAR(250) NULL ,
  `url` VARCHAR(250) NULL ,
  `info` VARCHAR(650) NULL ,
  `multiinfo` VARCHAR(250) NULL ,
  `reads` INT NULL ,
  `posts` INT NULL ,
  `insignificance` INT NULL ,
  `significance` INT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
COMMENT = '互联网资源';
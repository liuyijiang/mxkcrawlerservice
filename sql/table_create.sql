CREATE  TABLE `mxkdatabase`.`TB_WEB_RESOURCE` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(250) NOT NULL ,
  `image` VARCHAR(250) NOT NULL ,
  `ownername` VARCHAR(100) NULL ,
  `sitename` VARCHAR(100) NULL ,
  `siteurl` VARCHAR(100) NULL ,
  `url` VARCHAR(150) NOT NULL ,
  `info` VARCHAR(650) NULL ,
  `multiinfo` VARCHAR(250) NULL ,
  `hits` INT NULL ,
  `posts` INT NULL ,
  `insignificance` INT NULL ,
  `significance` INT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `url_UNIQUE` (`url` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COMMENT = '互联网资源';
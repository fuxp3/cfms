DROP TABLE IF EXISTS `t_action`;

CREATE TABLE `t_action` (
                            `id` bigint(20) DEFAULT NULL,
                            `type` varchar(96) DEFAULT NULL,
                            `name` varchar(765) DEFAULT NULL,
                            `uid` varchar(384) DEFAULT NULL,
                            `title` varchar(192) DEFAULT NULL,
                            `desc` varchar(765) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_action` */

insert  into `t_action`(`id`,`type`,`name`,`uid`,`title`,`desc`) values (23,'胸肌','1676552764972.mp4','1677243351217.mp4',NULL,NULL),(24,'胸肌','1676537164037.mp4','1677250673445.mp4',NULL,NULL);

/*Table structure for table `t_blog` */

DROP TABLE IF EXISTS `t_blog`;

CREATE TABLE `t_blog` (
                          `id` bigint(20) DEFAULT NULL,
                          `usercode` varchar(192) DEFAULT NULL,
                          `content` varchar(3072) DEFAULT NULL,
                          `state` varchar(192) DEFAULT NULL,
                          `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_blog` */

insert  into `t_blog`(`id`,`usercode`,`content`,`state`,`create_time`) values (2,'admin','你没权利看不惯我的生活方式，但你有权抠瞎自己的双眼。把闲言碎语留给市井小人，你只管优雅从容心怀远方。','已通过','0000-00-00 00:00:00'),(3,'admin','喜欢你的时候，你说什么是什么；不喜欢你的时候，你说你是什么！','已通过','0000-00-00 00:00:00'),(4,'admin','我从不奉承谁，嘴巴也不够甜，但你要令我失望，我就有资本让你绝望。','已通过','0000-00-00 00:00:00'),(5,'admin','我宁可别人说我轻佻骄傲，冷酷无情，也不想做个呼之即来挥之即去的老好人。','已通过','0000-00-00 00:00:00'),(6,'admin','不管你用什么方式活着，我们只有一个目的，别违心，以及别后悔，还有，去他的人言可畏。','已通过','0000-00-00 00:00:00'),(7,'admin','一辈子不长不短，要做自己喜欢的事，不要再对谁满怀期待，爱情算个屁，发财最要紧。','已通过','0000-00-00 00:00:00'),(8,'admin','别人怎么对你，你用相同的态度回应就是，生而为人，谁都是第一次！','已通过','0000-00-00 00:00:00'),(9,'admin','把闲言碎语留给市井小人，你只管优雅从容心怀远方。','已通过','2023-02-26 18:27:25'),(10,'admin','我很好','已通过','2023-02-26 23:09:41');

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment` (
                             `id` bigint(20) DEFAULT NULL,
                             `usercode` varchar(192) DEFAULT NULL,
                             `comment` varchar(3072) DEFAULT NULL,
                             `state` varchar(192) DEFAULT NULL,
                             `blog_id` bigint(20) DEFAULT NULL,
                             `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_comment` */

insert  into `t_comment`(`id`,`usercode`,`comment`,`state`,`blog_id`,`create_time`) values (13,'admin','test','已通过',2,'2023-02-26 17:55:15'),(14,'admin','test','已通过',2,'2023-02-26 17:57:40'),(15,'admin','test','已通过',2,'2023-02-26 17:59:35'),(16,'admin','fasdf','已通过',2,'2023-02-26 18:15:00'),(17,'admin','aaaaa','已通过',9,'2023-02-26 18:28:01'),(18,'admin','你是谁','已通过',10,'2023-02-26 23:10:18');

/*Table structure for table `t_tip` */

DROP TABLE IF EXISTS `t_tip`;

CREATE TABLE `t_tip` (
                         `id` bigint(20) DEFAULT NULL,
                         `title` varchar(192) DEFAULT NULL,
                         `tip` varchar(765) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_tip` */

insert  into `t_tip`(`id`,`title`,`tip`) values (30,NULL,'心态决定未来,健康由你承载。');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
                          `id` bigint(20) DEFAULT NULL,
                          `usercode` varchar(192) DEFAULT NULL,
                          `password` varchar(384) DEFAULT NULL,
                          `name` varchar(192) DEFAULT NULL,
                          `salt` varchar(384) DEFAULT NULL,
                          `create_time` datetime DEFAULT NULL,
                          `create_user` bigint(20) DEFAULT NULL,
                          `update_time` datetime DEFAULT NULL,
                          `update_user` bigint(20) DEFAULT NULL,
                          `avatar` varchar(384) DEFAULT NULL,
                          `email` varchar(384) DEFAULT NULL,
                          `phone` varchar(96) DEFAULT NULL,
                          `sex` tinyint(1) DEFAULT NULL,
                          `description` varchar(384) DEFAULT NULL,
                          `status` tinyint(1) DEFAULT NULL,
                          `user_type` tinyint(1) DEFAULT NULL,
                          `department` bigint(20) DEFAULT NULL,
                          `height` varchar(96) DEFAULT NULL,
                          `weight` varchar(96) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`usercode`,`password`,`name`,`salt`,`create_time`,`create_user`,`update_time`,`update_user`,`avatar`,`email`,`phone`,`sex`,`description`,`status`,`user_type`,`department`,`height`,`weight`) values (209,'admin','570506b3ebfc5b965febb8292b0d7fea','超级管理员','58c653ddcfcfd40f336091b2abdabd84','2023-02-19 21:31:45',1,'2023-02-26 16:31:56',209,'1677241819781.jpg','14321423@qq.com',NULL,1,NULL,1,0,NULL,'181cm','75kg');

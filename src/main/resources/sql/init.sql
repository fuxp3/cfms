DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `usercode` varchar(64)  NOT NULL COMMENT '用户名',
  `password` varchar(128)  NOT NULL COMMENT '密码',
  `name` varchar(64)  NOT NULL COMMENT '用户中文名',
  `salt` varchar(128)  NOT NULL COMMENT '盐值',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新人ID',
  `avatar` varchar(128)  NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(128)  NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(32)  NULL DEFAULT NULL COMMENT '手机',
  `sex` tinyint(1) NULL DEFAULT 1 COMMENT '性别 0女 1男 2不详',
  `description` varchar(128)  NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 0锁定 1有效',
  `user_type` tinyint(1) NULL DEFAULT 0 COMMENT '0普通用户 1管理员  2租户管理员',
  `paas_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对接paas平台，如来自paas平台不允许删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username`(`usercode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';



DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` VARCHAR(32)  NOT NULL COMMENT '角色名称',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `create_user` BIGINT(20) NOT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `update_user` BIGINT(20) NULL DEFAULT NULL COMMENT '更新人ID',
  `description` VARCHAR(100)  NULL DEFAULT NULL COMMENT '描述',
  `status` TINYINT(1) NULL DEFAULT 1 COMMENT '状态 0锁定 1有效',
  `role_type` TINYINT(1) NULL DEFAULT 2 COMMENT '角色类型：0系统管理员角色 1租户管理员角色 2其它角色',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='角色表';



DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '资源编号',
  `name` VARCHAR(32)  NOT NULL COMMENT '资源名称',
  `parent_id` BIGINT(20) NULL DEFAULT NULL COMMENT '父级ID',
  `url` VARCHAR(128)  NULL DEFAULT NULL COMMENT 'URL',
  `perms` VARCHAR(128)  NULL DEFAULT NULL COMMENT '权限标识',
  `type` VARCHAR(16)  NOT NULL COMMENT '类型：button、menu',
  `icon` VARCHAR(32)  NULL DEFAULT NULL COMMENT '菜单图标',
  `priority` BIGINT(20) NULL DEFAULT NULL COMMENT '排序',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `status` TINYINT(1) NULL DEFAULT 0 COMMENT '状态 0锁定 1有效',
  `description` VARCHAR(128)  NULL DEFAULT NULL COMMENT '描述',
  `code` VARCHAR(128)  NOT NULL COMMENT '菜单编码',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_unique`(`code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='资源表';



DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
   `role_id` bigint(20) NOT NULL COMMENT '角色ID',
   `menu_id` bigint(20) NOT NULL COMMENT '菜单/按钮ID',
   PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色资源关联表';



DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
   `user_id` bigint(20) NOT NULL COMMENT '用户ID',
   `role_id` bigint(20) NOT NULL COMMENT '角色ID',
   PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';





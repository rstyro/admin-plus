/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : admin

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-01-16 18:05:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_login
-- ----------------------------
DROP TABLE IF EXISTS `admin_login`;
CREATE TABLE `admin_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `last_login_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_login
-- ----------------------------
INSERT INTO `admin_login` VALUES ('15', '1', '2018-12-18 10:17:13');

-- ----------------------------
-- Table structure for admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_menu`;
CREATE TABLE `admin_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父级ID',
  `menu_name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `menu_url` varchar(50) DEFAULT '#' COMMENT '菜单地址',
  `menu_type` enum('2','1') DEFAULT '2' COMMENT '1 -- 系统菜单，2 -- 业务菜单',
  `menu_icon` varchar(50) DEFAULT '#' COMMENT '菜单Icon',
  `sort_num` int(11) DEFAULT '1' COMMENT '排序',
  `user_id` int(11) DEFAULT '1' COMMENT '创建这个菜单的用户id',
  `is_del` int(11) DEFAULT '0' COMMENT '1-- 删除状态，0 -- 正常',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_menu
-- ----------------------------
INSERT INTO `admin_menu` VALUES ('1', '0', '系统管理', '#', '1', 'fa fa-gears', '1', '1', '0', '2017-09-08 16:15:24', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('2', '1', '菜单管理', 'admin/menu/list', '1', '#', '1', '1', '0', '2019-01-16 18:03:44', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('3', '1', '角色管理', 'admin/role/list', '1', null, '2', '1', '0', '2019-01-16 18:03:45', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('4', '1', '用户管理', 'admin/user/list', '1', '', '3', '1', '0', '2019-01-16 18:03:48', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('5', '0', '商户管理', '#', '2', 'fa fa-tasks', '2', '1', '0', '2018-07-30 19:12:51', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('6', '5', '商户列表', '/member/list', '2', '', '1', '1', '1', '2018-12-19 18:34:36', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('7', '5', '测试', 'dailyDetail/list', '2', '', '1', '1', '0', '2018-12-19 18:35:43', null);
INSERT INTO `admin_menu` VALUES ('8', '0', '测试', '#', '1', 'fa fa-888', '1', '1', '1', '2018-12-19 18:36:03', null);

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) DEFAULT NULL COMMENT '角色名',
  `role_desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `rights` varchar(255) DEFAULT '0' COMMENT '最大权限的值',
  `add_qx` varchar(255) DEFAULT '0' COMMENT '添加权限',
  `del_qx` varchar(255) DEFAULT '0' COMMENT '删除权限',
  `edit_qx` varchar(255) DEFAULT '0' COMMENT '编辑权限',
  `query_qx` varchar(255) DEFAULT '0' COMMENT '查看权限',
  `user_id` varchar(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', '管理员', '管理员权限', '1267650600228229401496703205375', '190', '190', '190', '190', '1', '2019-01-03 15:52:41');
INSERT INTO `admin_role` VALUES ('2', 'tyro', '随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的', '190', '190', '160', '190', '190', '1', '2019-01-03 16:31:18');

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `password` varchar(50) DEFAULT NULL,
  `pic_path` varchar(200) DEFAULT '/images/logo.png' COMMENT '头像地址',
  `status` enum('unlock','lock') DEFAULT 'unlock',
  `sessionId` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES ('1', 'admin', '超级管理员', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'http://www.lrshuai.top/upload/user/20170612/05976238.png', 'unlock', '87906722879D6DFCC6032721B4935B00', '2017-08-18 13:57:32');
INSERT INTO `admin_user` VALUES ('2', 'rstyro', '管理员', '86f7e437faa5a7fce15d1ddcb9eaeaea377667b8', 'http://www.lrshuai.top/upload/user/20170612/05976238.png', 'unlock', '86633AC1E371190AAE1A14322EB87840', '2017-08-18 13:57:32');
INSERT INTO `admin_user` VALUES ('4', 'asdf', 'asdf', '', 'http://localhost:8800/show//20190103/1546504246866.jpg', 'unlock', 'EADF971A41F761702C2C93CA090BDB09', '2019-01-03 07:40:07');

-- ----------------------------
-- Table structure for admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_user_role
-- ----------------------------
INSERT INTO `admin_user_role` VALUES ('30', '4', '1', '2019-01-03 16:31:01');
INSERT INTO `admin_user_role` VALUES ('31', '4', '2', '2019-01-03 16:31:01');
INSERT INTO `admin_user_role` VALUES ('32', '2', '1', '2019-01-03 16:31:04');
INSERT INTO `admin_user_role` VALUES ('33', '2', '2', '2019-01-03 16:31:04');

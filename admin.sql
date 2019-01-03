/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : admin

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-08-03 18:23:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_login`;
CREATE TABLE `sys_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `last_login_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_login
-- ----------------------------
INSERT INTO `sys_login` VALUES ('1', '1', '2018-08-03 15:15:05');
INSERT INTO `sys_login` VALUES ('2', '1', '2018-08-03 15:37:20');
INSERT INTO `sys_login` VALUES ('3', '1', '2018-08-03 15:40:14');
INSERT INTO `sys_login` VALUES ('4', '1', '2018-08-03 15:41:50');
INSERT INTO `sys_login` VALUES ('5', '1', '2018-08-03 15:43:45');
INSERT INTO `sys_login` VALUES ('6', '1', '2018-08-03 18:22:09');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_url` varchar(50) DEFAULT '#',
  `menu_type` enum('2','1') DEFAULT '2' COMMENT '1 -- 系统菜单，2 -- 业务菜单',
  `menu_icon` varchar(50) DEFAULT '#',
  `sort_num` int(11) DEFAULT '1',
  `user_id` int(11) DEFAULT '1' COMMENT '创建这个菜单的用户id',
  `is_del` int(11) DEFAULT '0' COMMENT '1-- 删除状态，0 -- 正常',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', '#', '1', 'fa fa-gears', '1', '1', '0', '2017-09-08 16:15:24', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('2', '1', '菜单管理', 'menu/list', '1', '#', '1', '1', '0', '2017-09-12 11:28:09', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('3', '1', '角色管理', 'role/list', '1', null, '2', '1', '0', '2017-09-07 17:58:52', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('4', '1', '用户管理', 'user/list', '1', '', '3', '1', '0', '2017-09-12 09:44:48', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('5', '0', '商户管理', '#', '2', 'fa fa-tasks', '2', '1', '0', '2018-07-30 19:12:51', '2017-09-07 14:52:41');
INSERT INTO `sys_menu` VALUES ('6', '5', '商户列表', '/member/list', '2', '', '1', '1', '0', '2018-07-31 14:19:35', '2017-09-07 14:52:41');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) DEFAULT NULL COMMENT '角色名',
  `role_desc` varchar(255) DEFAULT NULL,
  `rights` varchar(255) DEFAULT '0' COMMENT '最大权限的值',
  `add_qx` varchar(255) DEFAULT '0',
  `del_qx` varchar(255) DEFAULT '0',
  `edit_qx` varchar(255) DEFAULT '0',
  `query_qx` varchar(255) DEFAULT '0',
  `user_id` varchar(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', '管理员权限', '1267650600228229401496703205375', '1', '1', '126', '126', '1', '2017-09-12 15:38:56');
INSERT INTO `sys_role` VALUES ('2', 'tyro', '随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的随便创建的', '94', '2', '1', '4', '126', '1', '2017-09-12 15:44:06');
INSERT INTO `sys_role` VALUES ('3', 'test', '是测试角色这个是测试角色这个是测试角色这个是测试角色这个是测试角色', '382', '382', '382', '382', '126', '1', '2017-09-12 15:39:28');
INSERT INTO `sys_role` VALUES ('4', '查看', '可以查看所有的东西', '126', '0', '0', '0', '126', '1', '2017-09-14 17:17:17');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `pic_path` varchar(200) DEFAULT '/images/logo.png',
  `status` enum('unlock','lock') DEFAULT 'unlock',
  `sessionId` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '管理员', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'http://www.lrshuai.top/upload/user/20170612/05976238.png', 'unlock', 'D7B7F5997D87AED1390270A5260BD8FF', '2017-08-18 13:57:32');
INSERT INTO `sys_user` VALUES ('2', 'tyro', 'tyro', '481c63e8b904bb8399f1fc1dfdb77cb40842eb6f', '/upload/show/user/82197046.png', 'unlock', null, '2017-09-12 14:03:39');
INSERT INTO `sys_user` VALUES ('3', 'asdf', 'asdf', '3da541559918a808c2402bba5012f6c60b27661c', '/upload/show/user/85610497.png', 'unlock', null, '2017-09-13 14:49:10');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1', '2017-08-18 14:45:43');
INSERT INTO `sys_user_role` VALUES ('2', '2', '3', '2017-09-08 17:12:58');
INSERT INTO `sys_user_role` VALUES ('13', '3', '3', '2017-09-14 14:30:02');

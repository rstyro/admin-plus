/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : admin

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2019-08-03 11:11:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `act_acticle`
-- ----------------------------
DROP TABLE IF EXISTS `act_acticle`;
CREATE TABLE `act_acticle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auther` varchar(20) DEFAULT NULL COMMENT '作者',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `is_deleted` varchar(10) DEFAULT 'N' COMMENT '是否删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1085810963130765316 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of act_acticle
-- ----------------------------
INSERT INTO `act_acticle` VALUES ('1', 'rstyro11', 'aabb', 'aabb', 'N', '2019-01-17 10:05:21', '2019-01-17 10:05:22');
INSERT INTO `act_acticle` VALUES ('97', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:02');
INSERT INTO `act_acticle` VALUES ('98', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:03');
INSERT INTO `act_acticle` VALUES ('99', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:03');
INSERT INTO `act_acticle` VALUES ('101', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('102', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('103', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('104', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('108', 'rstyro', 'aa', 'aa', 'Y', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('109', 'rstyro', 'aa', 'aa', 'Y', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('110', '帅大叔', '测试', '测试，我就是我，不一样的烟火', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('111', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('112', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('113', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('114', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('115', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('123', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('124', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('125', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('126', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('127', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('128', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('129', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');
INSERT INTO `act_acticle` VALUES ('130', 'rstyro', 'aa', 'aa', 'N', '2019-01-17 10:05:21', '2019-01-17 10:35:04');

-- ----------------------------
-- Table structure for `admin_login`
-- ----------------------------
DROP TABLE IF EXISTS `admin_login`;
CREATE TABLE `admin_login` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) NOT NULL,
  `last_login_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin_login
-- ----------------------------
INSERT INTO `admin_login` VALUES ('15', '1', '2018-12-18 10:17:13');
INSERT INTO `admin_login` VALUES ('16', '1', '2018-12-18 10:23:47');
INSERT INTO `admin_login` VALUES ('17', '1', '2018-12-18 10:24:49');
INSERT INTO `admin_login` VALUES ('18', '1', '2018-12-18 10:25:03');
INSERT INTO `admin_login` VALUES ('19', '2', '2018-12-18 10:28:09');
INSERT INTO `admin_login` VALUES ('20', '2', '2018-12-18 10:29:31');
INSERT INTO `admin_login` VALUES ('21', '2', '2018-12-18 10:29:33');
INSERT INTO `admin_login` VALUES ('22', '2', '2018-12-18 10:29:49');
INSERT INTO `admin_login` VALUES ('23', '2', '2018-12-18 10:30:39');
INSERT INTO `admin_login` VALUES ('24', '2', '2018-12-18 10:31:39');
INSERT INTO `admin_login` VALUES ('25', '2', '2018-12-18 10:35:06');
INSERT INTO `admin_login` VALUES ('26', '2', '2018-12-19 01:37:42');
INSERT INTO `admin_login` VALUES ('27', '2', '2018-12-19 01:59:16');
INSERT INTO `admin_login` VALUES ('28', '2', '2018-12-19 02:02:01');
INSERT INTO `admin_login` VALUES ('29', '2', '2018-12-19 02:04:06');
INSERT INTO `admin_login` VALUES ('30', '1', '2018-12-19 02:04:19');
INSERT INTO `admin_login` VALUES ('31', '2', '2018-12-19 02:07:07');
INSERT INTO `admin_login` VALUES ('32', '2', '2018-12-19 02:11:48');
INSERT INTO `admin_login` VALUES ('33', '1', '2018-12-19 02:12:01');
INSERT INTO `admin_login` VALUES ('34', '1', '2018-12-19 02:24:09');
INSERT INTO `admin_login` VALUES ('35', '1', '2018-12-19 02:47:56');
INSERT INTO `admin_login` VALUES ('36', '1', '2018-12-19 02:50:48');
INSERT INTO `admin_login` VALUES ('37', '1', '2018-12-19 02:51:58');
INSERT INTO `admin_login` VALUES ('38', '1', '2018-12-19 03:53:12');
INSERT INTO `admin_login` VALUES ('39', '1', '2018-12-19 03:56:38');
INSERT INTO `admin_login` VALUES ('40', '1', '2018-12-19 03:59:09');
INSERT INTO `admin_login` VALUES ('41', '1', '2018-12-19 04:49:58');
INSERT INTO `admin_login` VALUES ('42', '1', '2018-12-19 05:05:50');
INSERT INTO `admin_login` VALUES ('43', '1', '2018-12-19 05:07:28');
INSERT INTO `admin_login` VALUES ('44', '1', '2018-12-19 05:09:09');
INSERT INTO `admin_login` VALUES ('45', '1', '2018-12-19 05:14:12');
INSERT INTO `admin_login` VALUES ('46', '1', '2018-12-19 05:17:19');
INSERT INTO `admin_login` VALUES ('47', '1', '2018-12-19 06:05:29');
INSERT INTO `admin_login` VALUES ('48', '1', '2018-12-19 08:13:07');
INSERT INTO `admin_login` VALUES ('49', '1', '2018-12-19 08:14:12');
INSERT INTO `admin_login` VALUES ('50', '1', '2018-12-19 08:18:26');
INSERT INTO `admin_login` VALUES ('51', '1', '2018-12-19 08:20:54');
INSERT INTO `admin_login` VALUES ('52', '1', '2018-12-19 08:23:04');
INSERT INTO `admin_login` VALUES ('53', '1', '2018-12-19 08:23:47');
INSERT INTO `admin_login` VALUES ('54', '1', '2018-12-19 08:27:08');
INSERT INTO `admin_login` VALUES ('55', '1', '2018-12-19 08:51:07');
INSERT INTO `admin_login` VALUES ('56', '1', '2018-12-19 09:20:47');
INSERT INTO `admin_login` VALUES ('57', '1', '2018-12-19 10:31:56');
INSERT INTO `admin_login` VALUES ('58', '1', '2018-12-19 10:34:23');
INSERT INTO `admin_login` VALUES ('59', '1', '2018-12-25 09:28:34');
INSERT INTO `admin_login` VALUES ('60', '1', '2018-12-26 03:16:07');
INSERT INTO `admin_login` VALUES ('61', '1', '2018-12-28 08:34:14');
INSERT INTO `admin_login` VALUES ('62', '1', '2018-12-28 11:36:03');
INSERT INTO `admin_login` VALUES ('63', '1', '2018-12-28 11:39:57');
INSERT INTO `admin_login` VALUES ('64', '1', '2018-12-28 11:40:41');
INSERT INTO `admin_login` VALUES ('65', '1', '2018-12-29 03:03:14');
INSERT INTO `admin_login` VALUES ('66', '1', '2018-12-29 12:58:21');
INSERT INTO `admin_login` VALUES ('67', '1', '2018-12-29 13:00:24');
INSERT INTO `admin_login` VALUES ('68', '1', '2018-12-29 13:04:47');
INSERT INTO `admin_login` VALUES ('69', '1', '2018-12-29 13:07:17');
INSERT INTO `admin_login` VALUES ('70', '1', '2018-12-29 13:40:09');
INSERT INTO `admin_login` VALUES ('71', '1', '2018-12-29 13:41:59');
INSERT INTO `admin_login` VALUES ('72', '1', '2018-12-29 14:00:47');
INSERT INTO `admin_login` VALUES ('73', '1', '2018-12-29 14:03:06');
INSERT INTO `admin_login` VALUES ('74', '1', '2018-12-29 14:04:31');
INSERT INTO `admin_login` VALUES ('75', '1', '2018-12-29 14:17:42');
INSERT INTO `admin_login` VALUES ('76', '1', '2018-12-29 14:19:44');
INSERT INTO `admin_login` VALUES ('77', '1', '2018-12-29 14:35:58');
INSERT INTO `admin_login` VALUES ('78', '1', '2018-12-29 15:15:51');
INSERT INTO `admin_login` VALUES ('79', '1', '2018-12-29 15:41:25');
INSERT INTO `admin_login` VALUES ('80', '1', '2019-01-03 07:37:29');
INSERT INTO `admin_login` VALUES ('81', '1', '2019-01-03 07:39:25');
INSERT INTO `admin_login` VALUES ('82', '1', '2019-01-03 07:42:43');
INSERT INTO `admin_login` VALUES ('83', '4', '2019-01-03 07:43:08');
INSERT INTO `admin_login` VALUES ('84', '1', '2019-01-03 07:43:17');
INSERT INTO `admin_login` VALUES ('85', '4', '2019-01-03 07:43:51');
INSERT INTO `admin_login` VALUES ('86', '1', '2019-01-03 07:44:01');
INSERT INTO `admin_login` VALUES ('87', '4', '2019-01-03 07:46:04');
INSERT INTO `admin_login` VALUES ('88', '1', '2019-01-03 07:47:40');
INSERT INTO `admin_login` VALUES ('89', '4', '2019-01-03 07:47:45');
INSERT INTO `admin_login` VALUES ('90', '1', '2019-01-03 07:49:05');
INSERT INTO `admin_login` VALUES ('91', '4', '2019-01-03 07:49:11');
INSERT INTO `admin_login` VALUES ('92', '4', '2019-01-03 07:50:52');
INSERT INTO `admin_login` VALUES ('93', '1', '2019-01-03 07:51:59');
INSERT INTO `admin_login` VALUES ('94', '4', '2019-01-03 07:52:04');
INSERT INTO `admin_login` VALUES ('95', '1', '2019-01-03 07:52:28');
INSERT INTO `admin_login` VALUES ('96', '4', '2019-01-03 07:52:59');
INSERT INTO `admin_login` VALUES ('97', '1', '2019-01-03 07:53:12');
INSERT INTO `admin_login` VALUES ('98', '4', '2019-01-03 07:53:26');
INSERT INTO `admin_login` VALUES ('99', '4', '2019-01-03 07:59:22');
INSERT INTO `admin_login` VALUES ('100', '1', '2019-01-03 07:59:33');
INSERT INTO `admin_login` VALUES ('101', '4', '2019-01-03 08:01:25');
INSERT INTO `admin_login` VALUES ('102', '4', '2019-01-03 08:03:28');
INSERT INTO `admin_login` VALUES ('103', '4', '2019-01-03 08:04:41');
INSERT INTO `admin_login` VALUES ('104', '1', '2019-01-03 08:05:44');
INSERT INTO `admin_login` VALUES ('105', '1', '2019-01-03 08:06:54');
INSERT INTO `admin_login` VALUES ('106', '1', '2019-01-03 08:30:21');
INSERT INTO `admin_login` VALUES ('107', '1', '2019-01-04 09:23:18');
INSERT INTO `admin_login` VALUES ('108', '1', '2019-01-04 09:23:44');
INSERT INTO `admin_login` VALUES ('109', '1', '2019-01-04 09:26:11');
INSERT INTO `admin_login` VALUES ('110', '2', '2019-01-16 18:03:26');
INSERT INTO `admin_login` VALUES ('111', '2', '2019-01-16 18:03:54');
INSERT INTO `admin_login` VALUES ('112', '2', '2019-01-17 09:57:13');
INSERT INTO `admin_login` VALUES ('113', '2', '2019-01-17 09:59:33');
INSERT INTO `admin_login` VALUES ('114', '2', '2019-01-17 10:00:09');
INSERT INTO `admin_login` VALUES ('115', '2', '2019-01-17 10:02:05');
INSERT INTO `admin_login` VALUES ('116', '2', '2019-01-17 10:04:10');
INSERT INTO `admin_login` VALUES ('117', '2', '2019-01-17 10:06:49');
INSERT INTO `admin_login` VALUES ('118', '2', '2019-01-17 10:10:36');
INSERT INTO `admin_login` VALUES ('119', '2', '2019-01-17 10:30:38');
INSERT INTO `admin_login` VALUES ('120', '2', '2019-01-17 10:33:10');
INSERT INTO `admin_login` VALUES ('121', '2', '2019-01-17 10:34:20');
INSERT INTO `admin_login` VALUES ('122', '2', '2019-01-17 10:42:04');
INSERT INTO `admin_login` VALUES ('123', '2', '2019-01-17 11:02:00');
INSERT INTO `admin_login` VALUES ('124', '2', '2019-01-17 11:05:55');
INSERT INTO `admin_login` VALUES ('125', '2', '2019-01-17 11:07:29');
INSERT INTO `admin_login` VALUES ('126', '2', '2019-01-17 11:13:20');
INSERT INTO `admin_login` VALUES ('127', '2', '2019-01-17 11:14:12');
INSERT INTO `admin_login` VALUES ('128', '2', '2019-01-17 11:15:53');
INSERT INTO `admin_login` VALUES ('129', '2', '2019-01-17 15:03:22');
INSERT INTO `admin_login` VALUES ('130', '2', '2019-01-17 15:06:58');
INSERT INTO `admin_login` VALUES ('131', '2', '2019-01-17 15:11:12');
INSERT INTO `admin_login` VALUES ('132', '2', '2019-01-17 15:21:34');
INSERT INTO `admin_login` VALUES ('133', '2', '2019-01-17 15:25:22');
INSERT INTO `admin_login` VALUES ('134', '2', '2019-01-17 15:28:07');
INSERT INTO `admin_login` VALUES ('135', '2', '2019-01-17 15:39:50');
INSERT INTO `admin_login` VALUES ('136', '2', '2019-01-17 15:43:49');
INSERT INTO `admin_login` VALUES ('137', '2', '2019-01-17 15:50:10');
INSERT INTO `admin_login` VALUES ('138', '2', '2019-01-17 15:55:19');
INSERT INTO `admin_login` VALUES ('139', '2', '2019-01-17 16:01:11');
INSERT INTO `admin_login` VALUES ('140', '2', '2019-01-17 16:04:03');
INSERT INTO `admin_login` VALUES ('141', '2', '2019-01-17 16:11:15');
INSERT INTO `admin_login` VALUES ('142', '2', '2019-01-17 16:14:40');
INSERT INTO `admin_login` VALUES ('143', '2', '2019-01-17 16:19:00');
INSERT INTO `admin_login` VALUES ('144', '2', '2019-01-17 16:21:42');
INSERT INTO `admin_login` VALUES ('145', '2', '2019-01-18 11:14:53');
INSERT INTO `admin_login` VALUES ('146', '2', '2019-01-18 11:17:30');
INSERT INTO `admin_login` VALUES ('147', '2', '2019-01-18 11:54:59');
INSERT INTO `admin_login` VALUES ('148', '2', '2019-01-18 14:54:54');
INSERT INTO `admin_login` VALUES ('149', '2', '2019-01-18 14:59:47');
INSERT INTO `admin_login` VALUES ('150', '2', '2019-01-18 15:00:24');
INSERT INTO `admin_login` VALUES ('151', '2', '2019-08-03 10:20:41');
INSERT INTO `admin_login` VALUES ('152', '2', '2019-08-03 10:23:24');
INSERT INTO `admin_login` VALUES ('153', '2', '2019-08-03 10:50:24');
INSERT INTO `admin_login` VALUES ('154', '2', '2019-08-03 10:57:04');
INSERT INTO `admin_login` VALUES ('155', '2', '2019-08-03 10:59:51');
INSERT INTO `admin_login` VALUES ('156', '2', '2019-08-03 11:00:22');
INSERT INTO `admin_login` VALUES ('157', '2', '2019-08-03 11:09:23');

-- ----------------------------
-- Table structure for `admin_menu`
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin_menu
-- ----------------------------
INSERT INTO `admin_menu` VALUES ('1', '0', '系统管理', '#', '1', 'fa fa-gears', '1', '1', '0', '2017-09-08 16:15:24', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('2', '1', '菜单管理', 'admin/menu/list', '1', '#', '1', '1', '0', '2019-01-16 18:03:44', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('3', '1', '角色管理', 'admin/role/list', '1', null, '2', '1', '0', '2019-01-16 18:03:45', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('4', '1', '用户管理', 'admin/user/list', '1', '', '3', '1', '0', '2019-01-16 18:03:48', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('5', '0', '测试管理', '#', '2', 'fa fa-tasks', '2', '1', '0', '2019-08-03 11:00:15', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('6', '5', '文章列表', 'act/acticle/list', '2', '', '1', '1', '0', '2019-08-03 11:00:02', '2017-09-07 14:52:41');
INSERT INTO `admin_menu` VALUES ('7', '5', '测试', 'test/people/list', '2', '', '2', '1', '0', '2019-08-03 11:09:46', null);

-- ----------------------------
-- Table structure for `admin_role`
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', '管理员', '管理员权限', '1267650600228229401496703205375', '254', '254', '254', '254', '1', '2019-01-17 09:59:29');
INSERT INTO `admin_role` VALUES ('2', 'tyro', '111', '254', '254', '160', '190', '254', '1', '2019-08-03 11:09:59');

-- ----------------------------
-- Table structure for `admin_user`
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `password` varchar(50) DEFAULT NULL,
  `pic_path` varchar(200) DEFAULT '/images/logo.png' COMMENT '头像地址',
  `status` enum('unlock','lock') DEFAULT 'unlock',
  `sessionId` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES ('1', 'admin', '超级管理员', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'http://www.lrshuai.top/upload/user/20170612/05976238.png', 'unlock', '87906722879D6DFCC6032721B4935B00', '2017-08-18 13:57:32');
INSERT INTO `admin_user` VALUES ('2', 'rstyro', '管理员', '86f7e437faa5a7fce15d1ddcb9eaeaea377667b8', 'http://www.lrshuai.top/upload/user/20170612/05976238.png', 'unlock', '2479928844EB9E0C7C35914DF696D819', '2017-08-18 13:57:32');
INSERT INTO `admin_user` VALUES ('4', 'asdf', 'asdf', '', 'http://localhost:8800/show//20190803/1564801821805.jpg', 'unlock', 'EADF971A41F761702C2C93CA090BDB09', '2019-01-03 07:40:07');

-- ----------------------------
-- Table structure for `admin_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin_user_role
-- ----------------------------
INSERT INTO `admin_user_role` VALUES ('32', '2', '1', '2019-01-03 16:31:04');
INSERT INTO `admin_user_role` VALUES ('33', '2', '2', '2019-01-03 16:31:04');
INSERT INTO `admin_user_role` VALUES ('34', '4', '1', '2019-08-03 10:51:08');

-- ----------------------------
-- Table structure for `test_people`
-- ----------------------------
DROP TABLE IF EXISTS `test_people`;
CREATE TABLE `test_people` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `age` int(5) DEFAULT NULL COMMENT '年龄',
  `sex` int(1) DEFAULT '1' COMMENT '性别：1--男，0 -- 女',
  `is_deleted` varchar(2) DEFAULT 'N' COMMENT '是否删除，Y/N',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test_people
-- ----------------------------
INSERT INTO `test_people` VALUES ('1', 'rstyro', '24', '1', 'N', '1', '2019-01-18 11:44:24', '1', '2019-01-18 11:44:26');

/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : admin

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 10/12/2023 20:40:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_btn
-- ----------------------------
DROP TABLE IF EXISTS `sys_btn`;
CREATE TABLE `sys_btn`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `btn_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '按钮名称',
  `btn_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单下的按钮权限通用key',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_btn
-- ----------------------------
INSERT INTO `sys_btn` VALUES (1, '删除', 'del', '删除', 1, '2023-12-04 21:31:14');
INSERT INTO `sys_btn` VALUES (2, '添加', 'add', '添加', 1, '2023-12-04 21:31:21');
INSERT INTO `sys_btn` VALUES (3, '编辑', 'edit', '编辑', 1, '2023-12-04 21:31:28');
INSERT INTO `sys_btn` VALUES (4, '查看', 'view', '查看', 1, '2023-12-04 22:25:20');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父级ID',
  `menu_type` int(11) NULL DEFAULT 1 COMMENT '0=目录，1=菜单，2-按钮',
  `menu_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单名称',
  `menu_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单地址',
  `permit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单许可Key',
  `sort_num` int(11) NULL DEFAULT 1 COMMENT '排序',
  `icon` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单icon',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, 0, '系统管理', '', NULL, 0, 'fa fa-gears', 1, '2023-12-01 21:31:35');
INSERT INTO `sys_menu` VALUES (2, 1, 1, '菜单列表', 'system/menu/page', 'system:menu:list', 1, 'fa fa-circle-o', 1, '2023-12-01 21:32:57');
INSERT INTO `sys_menu` VALUES (4, 1, 1, '用户列表', 'system/user/page', 'system:user:list', 2, 'fa fa-circle-o', NULL, NULL);
INSERT INTO `sys_menu` VALUES (45, 43, 1, 'test', 'test', 'test', NULL, NULL, 1, '2023-12-02 20:36:02');
INSERT INTO `sys_menu` VALUES (46, 0, 0, 'test', 'test', NULL, 1, 'fa fa-hand-peace-o', 1, '2023-12-02 22:20:39');
INSERT INTO `sys_menu` VALUES (47, 46, 1, 'ICON', 'icon', NULL, NULL, 'fa fa-user', 1, '2023-12-02 22:20:54');
INSERT INTO `sys_menu` VALUES (48, 1, 1, '角色列表', 'system/role/page', 'system:role:list', 3, 'fa fa-circle-o', 1, '2023-12-02 22:24:05');
INSERT INTO `sys_menu` VALUES (49, 1, 1, '公用按钮', 'system/btn/page', 'system:btn:list', 1, NULL, 1, '2023-12-04 21:30:52');
INSERT INTO `sys_menu` VALUES (50, 2, 2, 'del', NULL, 'system:menu:list:del', 1, NULL, 1, '2023-12-04 21:40:11');
INSERT INTO `sys_menu` VALUES (51, 2, 2, 'add', NULL, 'system:menu:list:add', 1, NULL, 1, '2023-12-04 21:40:11');
INSERT INTO `sys_menu` VALUES (52, 2, 2, 'edit', NULL, 'system:menu:list:edit', 1, NULL, 1, '2023-12-04 21:40:11');
INSERT INTO `sys_menu` VALUES (53, 2, 2, 'view', NULL, 'system:menu:list:view', 1, NULL, 1, '2023-12-04 22:25:31');
INSERT INTO `sys_menu` VALUES (54, 4, 2, 'del', NULL, 'system:user:list:del', 1, NULL, 1, '2023-12-04 22:49:41');
INSERT INTO `sys_menu` VALUES (55, 4, 2, 'add', NULL, 'system:user:list:add', 1, NULL, 1, '2023-12-04 22:49:41');
INSERT INTO `sys_menu` VALUES (56, 4, 2, 'edit', NULL, 'system:user:list:edit', 1, NULL, 1, '2023-12-04 22:49:42');
INSERT INTO `sys_menu` VALUES (57, 4, 2, 'view', NULL, 'system:user:list:view', 1, NULL, 1, '2023-12-04 22:49:42');
INSERT INTO `sys_menu` VALUES (58, 48, 2, 'del', NULL, 'system:role:list:del', 1, NULL, 1, '2023-12-04 22:49:53');
INSERT INTO `sys_menu` VALUES (59, 48, 2, 'add', NULL, 'system:role:list:add', 1, NULL, 1, '2023-12-04 22:49:53');
INSERT INTO `sys_menu` VALUES (60, 48, 2, 'edit', NULL, 'system:role:list:edit', 1, NULL, 1, '2023-12-04 22:49:53');
INSERT INTO `sys_menu` VALUES (61, 48, 2, 'view', NULL, 'system:role:list:view', 1, NULL, 1, '2023-12-04 22:49:53');
INSERT INTO `sys_menu` VALUES (62, 49, 2, 'del', NULL, 'system:btn:list:del', 1, NULL, 1, '2023-12-04 22:57:53');
INSERT INTO `sys_menu` VALUES (63, 49, 2, 'add', NULL, 'system:btn:list:add', 1, NULL, 1, '2023-12-04 22:57:53');
INSERT INTO `sys_menu` VALUES (64, 49, 2, 'edit', NULL, 'system:btn:list:edit', 1, NULL, 1, '2023-12-04 22:57:53');
INSERT INTO `sys_menu` VALUES (65, 49, 2, 'view', NULL, 'system:btn:list:view', 1, NULL, 1, '2023-12-04 22:57:53');
INSERT INTO `sys_menu` VALUES (66, 4, 2, '权限赋予', NULL, 'system:user:list:grant', 1, NULL, 7, '2023-12-04 23:10:20');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(20) NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type`) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status`) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色备注',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1157861380 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (289550337, '测试', '测试', 1, '2023-12-03 21:46:56');
INSERT INTO `sys_role` VALUES (532901890, '管理员', '管理员', 1, '2023-12-02 22:52:06');
INSERT INTO `sys_role` VALUES (1157861378, '超级管理员', 'admin', 1, '2023-12-02 22:49:09');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_id` bigint(255) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  `create_by` bigint(255) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 186 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 532901890, 1, '2023-12-04 21:19:55');
INSERT INTO `sys_role_menu` VALUES (2, 2, 532901890, 1, '2023-12-04 21:19:55');
INSERT INTO `sys_role_menu` VALUES (3, 4, 532901890, 1, '2023-12-04 21:19:55');
INSERT INTO `sys_role_menu` VALUES (4, 48, 532901890, 1, '2023-12-04 21:19:55');
INSERT INTO `sys_role_menu` VALUES (8, 1, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (9, 2, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (10, 50, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (11, 51, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (12, 52, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (13, 49, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (14, 4, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (15, 48, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (16, 46, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (17, 47, 1157861378, 1, '2023-12-04 21:40:53');
INSERT INTO `sys_role_menu` VALUES (162, 1, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (163, 2, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (164, 53, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (165, 52, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (166, 50, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (167, 51, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (168, 49, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (169, 62, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (170, 63, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (171, 64, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (172, 65, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (173, 4, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (174, 57, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (175, 54, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (176, 55, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (177, 56, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (178, 66, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (179, 48, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (180, 60, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (181, 58, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (182, 59, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (183, 61, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (184, 46, 289550337, 1, '2023-12-04 23:16:19');
INSERT INTO `sys_role_menu` VALUES (185, 47, 289550337, 1, '2023-12-04 23:16:19');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '昵称',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码随机盐',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户头像',
  `status` int(11) NULL DEFAULT 1 COMMENT '用户状态：1-正常，2-已锁定',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '最后登录IP',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '超级管理员 ', 'admin', '4a5a3422434efd5f7f432cf844df07f5', '49d11245f5c8c8736b6ad62876075c78', '/show/20231205/1701788569670.png', 1, 1, '2023-12-10 11:53:02', '192.168.1.105', '2023-12-05 22:56:39', NULL, '2023-12-02 22:08:10');
INSERT INTO `sys_user` VALUES (7, 'test', 'test', '49d11245f5c8c8736b6ad62876075c78', '653ccf53a4cd47029a308ac2a541ba00', '/show/20231202/1701526801651.png', 1, NULL, NULL, NULL, NULL, 1, '2023-12-02 22:20:07');
INSERT INTO `sys_user` VALUES (9, 'abc', 'abc', 'a8fbcd5503bc21d347a8dc4956f15827', 'a0ce1b00ec224c9c8b384fdfaf865967', '/show/20231209/1702097065890.png', 1, 1, NULL, NULL, '2023-12-09 12:44:28', 1, '2023-12-09 12:33:43');

-- ----------------------------
-- Table structure for sys_user_online
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_online`;
CREATE TABLE `sys_user_online`  (
  `sessionId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户会话id',
  `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '登录账号',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '部门名称',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '操作系统',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '在线状态on_line在线off_line离线',
  `start_timestamp` datetime NULL DEFAULT NULL COMMENT 'session创建时间',
  `last_access_time` datetime NULL DEFAULT NULL COMMENT 'session最后访问时间',
  `expire_time` int(5) NULL DEFAULT 0 COMMENT '超时时间，单位为分钟',
  PRIMARY KEY (`sessionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '在线用户记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_online
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (39, 7, 289550337, '2023-12-04 21:41:32');

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES (1733351958015848449, NULL, NULL, NULL);
INSERT INTO `test` VALUES (1733352330801393666, NULL, NULL, NULL);
INSERT INTO `test` VALUES (1733352473344815106, NULL, NULL, NULL);
INSERT INTO `test` VALUES (1733352683148095489, NULL, NULL, NULL);
INSERT INTO `test` VALUES (1733352849011847169, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;

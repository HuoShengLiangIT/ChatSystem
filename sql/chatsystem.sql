/*
 Navicat Premium Data Transfer

 Source Server         : mysql80
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : chatsystem

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 14/01/2019 20:31:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for offline_msg
-- ----------------------------
DROP TABLE IF EXISTS `offline_msg`;
CREATE TABLE `offline_msg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'message id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT 'user id',
  `to_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'reciver name',
  `from_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sender name',
  `msg_type` int(2) NULL DEFAULT NULL COMMENT 'message type 1:message 2:File',
  `msg` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'msg_type=1,content;=2,File Path',
  `state` int(2) NULL DEFAULT NULL COMMENT '1:not send,2:success,3:failure',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of offline_msg
-- ----------------------------
INSERT INTO `offline_msg` VALUES (1, NULL, 'tiantian', 'admin', 1, '123', 2);
INSERT INTO `offline_msg` VALUES (2, NULL, 'admin', 'tiantian', 1, '你好么？离线消息', 2);
INSERT INTO `offline_msg` VALUES (3, NULL, 'tiantian', 'admin', 1, 'file', 2);
INSERT INTO `offline_msg` VALUES (7, NULL, 'tiantian', 'admin', 2, '/C:/Users/14831/IdeaProjects/ChatSystem/Server\\数学建模.docx', 2);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'User id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'user name',
  `pwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'password',
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'email',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', '1483104508@qq.com');
INSERT INTO `user` VALUES (2, 'tiantian', '123456', '1483104508@qq.com');
INSERT INTO `user` VALUES (3, 'admin2', '1234567', '1234567890@qq.com');

SET FOREIGN_KEY_CHECKS = 1;

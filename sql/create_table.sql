# 建表脚本

-- 创建库
create database if not exists myworld;

-- 切换库
use myworld;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
    ) comment '用户' collate = utf8mb4_unicode_ci;

-- 图表表
create table if not exists chart
(
    id           bigint auto_increment comment 'id' primary key,
    goal				 text  null comment '分析目标',
    `name`               varchar(128) null comment '图表名称',
    chartData    text  null comment '图表数据',
    chartType	   varchar(128) null comment '图表类型',
    genChart		 text	 null comment '生成的图表数据',
    genResult		 text	 null comment '生成的分析结论',
    status       varchar(128) not null default 'wait' comment 'wait,running,succeed,failed',
    execMessage  text   null comment '执行信息',
    userId       bigint null comment '创建用户 id',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;

-- 文章表
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '文章id',
                             `title` varchar(200) NOT NULL DEFAULT '' COMMENT '文章标题',
                             `title_image` varchar(200) DEFAULT '' COMMENT '文章题图',
                             `description` varchar(160) NOT NULL DEFAULT '' COMMENT '文章描述',
                             `userId` bigint NOT NULL COMMENT '创建用户 id',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
                             `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志位：0：未删除 1：已删除',
                             `read_num` int unsigned NOT NULL DEFAULT '1' COMMENT '被阅读次数',
                             `thumbNum` int NOT NULL DEFAULT '0' COMMENT '点赞数',
                             `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏数',
                             PRIMARY KEY (`id`) USING BTREE,
                             KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章表';

-- 文章分类表
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
                              `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '标签id',
                              `name` varchar(60) NOT NULL DEFAULT '' COMMENT '分类名称',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
                              `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志位：0：未删除 1：已删除',
                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE KEY `uni_name` (`name`) USING BTREE,
                              KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章分类表';

-- 文章内容表
DROP TABLE IF EXISTS `t_article_content`;
CREATE TABLE `t_article_content` (
                                     `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '文章内容id',
                                     `article_id` bigint NOT NULL COMMENT '文章id',
                                     `content` text COMMENT '正文内容',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     KEY `idx_article_id` (`article_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章内容表';

-- 文章-分类-关联表
DROP TABLE IF EXISTS `t_article_category_rel`;
CREATE TABLE `t_article_category_rel` (
                                          `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                                          `article_id` bigint unsigned NOT NULL COMMENT '文章id',
                                          `category_id` bigint unsigned NOT NULL COMMENT '分类id',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          UNIQUE KEY `uni_article_id` (`article_id`) USING BTREE,
                                          KEY `idx_category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章所属分类映射表';

-- 这个是User模块的数据库
create database User
use User
CREATE TABLE User (
                      `user_id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
                      `username` varchar(16) NOT NULL COMMENT '用户名',
                      `password` varchar(20) NOT NULL COMMENT '密码',
                      `personal_signature` varchar(60) not null DEFAULT '' COMMENT '个性签名',
                      `header_image_adder` varchar(50) not null DEFAULT '' COMMENT '头像地址',
                      `sex` int not null DEFAULT 0 COMMENT '性别',
                      `birthday` DATE not null comment '生日',
                      `phone_number` varchar(11) unique not null COMMENT '手机号',
                      `follower_amount` int DEFAULT 0 COMMENT '粉丝数',
                      `liker_amount` int DEFAULT 0 COMMENT '关注数',
                      `writing_amount` int DEFAULT 0 COMMENT '文章数',
                      `question_amount` int DEFAULT 0 COMMENT '提问数',
                      `like_amount` int DEFAULT 0 COMMENT '点赞数',
                      `collect_amount` int DEFAULT 0 COMMENT '收藏数',
                      `message_power` int DEFAULT 1 COMMENT '信息展示权限',
                      `create_time` DATETIME not null COMMENT '创建时间',
                      `is_exist` int DEFAULT 1 COMMENT '是否删除：1存在 0删除',
                       index idx_phone (phone_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户表';


-- 这个是Social模块的数据库
create database Social
use Social
create table Social_Follow(
                              id bigint primary key auto_increment comment '纯主键id',
                              user_id bigint not null comment '用户id',
                              liker_id bigint not null comment '被关注方的用户id',
                              is_follow int not null comment '收藏状态',
                              unique key uk_liker_user (user_id,liker_id),
                              index idx_user (user_id),
                              index idx_liker (liker_id)
)


-- 这个是KnowAsk模块的数据库
create database KnowAsk
use KnowAsk
CREATE TABLE Question (
                          `question_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '提问ID',
                          `user_id` BIGINT NOT NULL COMMENT '用户ID',
                          `question_title` VARCHAR(20) NOT NULL COMMENT '提问标题',
                          `question_description` TEXT COMMENT '提问描述',
                          `image_adder_list` VARCHAR(200) DEFAULT '[]' COMMENT '插图地址列表',
                          `writing_amount` INT NOT NULL DEFAULT 0 COMMENT '被别的文章回复数',
                          `like_amount` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
                          `collect_amount` INT NOT NULL DEFAULT 0 COMMENT '被收藏数',
                          `is_exist` TINYINT NOT NULL DEFAULT 1 COMMENT '是否存在 1-存在 0-删除',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          INDEX idx_user_id (`user_id`),
                          INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提问表';

CREATE TABLE `Writing` (
                           `writing_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文章ID',
                           `question_id` BIGINT default 0 comment '提问id',
                           `user_id` BIGINT NOT NULL COMMENT '用户ID',
                           `writing_title` VARCHAR(20) NOT NULL COMMENT '文章标题',
                           `writing_description` TEXT COMMENT '文章描述',
                           `image_adder_list` VARCHAR(200) default '[]' COMMENT '插图地址列表',
                           `like_amount` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
                           `comment_amount` INT NOT NULL DEFAULT 0 COMMENT '评论数',
                           `collect_amount` INT NOT NULL DEFAULT 0 COMMENT '被收藏数',
                           `message_power` INT NOT NULL DEFAULT 1 COMMENT '信息展示权限',
                           `is_exist` TINYINT NOT NULL DEFAULT 1 COMMENT '是否存在 1-存在 0-删除',
                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           INDEX idx_question_id (`question_id`),
                           INDEX idx_user_id (`user_id`),
                           INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章主表';

-- 这个是Comment模块的数据库
create database Comment
use Comment
CREATE TABLE First_Comment (
                               first_comment_id bigint PRIMARY KEY COMMENT '一级评论ID',
                               user_id bigint NOT NULL COMMENT '用户ID',
                               writing_id bigint NOT NULL COMMENT '文章ID',
                               text VARCHAR(200) NOT NULL COMMENT '评论内容',
                               image_adder VARCHAR(50) DEFAULT '' COMMENT '插图地址',
                               second_comment_amount INT DEFAULT 0 COMMENT '拥有的二级评论数',
                               like_amount INT DEFAULT 0 COMMENT '拥有的点赞数',
                               create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               is_exist INT DEFAULT 1 COMMENT '是否存在',
                               INDEX idx_like_amount (`writing_id`,`like_amount`),
                               INDEX idx_create_time (`writing_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE Second_Comment (
                                second_comment_id bigint PRIMARY KEY COMMENT '二级评论ID',
                                user_id bigint NOT NULL COMMENT '用户ID',
                                first_comment_id bigint NOT NULL COMMENT '一级评论ID',
                                text VARCHAR(200) NOT NULL COMMENT '评论内容',
                                respond_username varchar(16) DEFAULT '' COMMENT '回复的用户姓名',
                                like_amount INT default 0 COMMENT '拥有的点赞数',
                                create_time DATETIME default CURRENT_TIMESTAMP COMMENT '创建时间',
                                is_exist INT default 1 COMMENT '是否存在',
                                INDEX idx_create_time (`first_comment_id`,`is_exist`,`like_amount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 这个是Appreciation模块的数据库
create database Appreciation
use Appreciation
create table Like_Writing(
                             id bigint primary key auto_increment comment '纯主键id',
                             user_id bigint not null comment '用户id',
                             writing_id bigint not null comment '文章的id',
                             is_like int not null comment '点赞状态',
                             unique key uk_writing_user (user_id,writing_id),
                             index idx_user (user_id),
                             index idx_writing (writing_id)
)
create table Like_Question(
                              id bigint primary key auto_increment comment '纯主键id',
                              user_id bigint not null comment '用户id',
                              question_id bigint not null comment '提问的id',
                              is_like int not null comment '点赞状态',
                              unique key uk_question_user (user_id,question_id),
                              index idx_user (user_id),
                              index idx_question (question_id)
)
create table Like_First_Comment(
                                   id bigint primary key auto_increment comment '纯主键id',
                                   user_id bigint not null comment '用户id',
                                   first_comment_id bigint not null comment '一级评论的id',
                                   is_like int not null comment '点赞状态',
                                   unique key uk_first_comment_user (user_id,first_comment_id),
                                   index idx_user (user_id),
                                   index idx_first_comment (first_comment_id)
)
create table Like_Second_Comment(
                                    id bigint primary key auto_increment comment '纯主键id',
                                    user_id bigint not null comment '用户id',
                                    second_comment_id bigint not null comment '二级评论的id',
                                    is_like int not null comment '点赞状态',
                                    unique key uk_second_comment_user (user_id,second_comment_id),
                                    index idx_user (user_id),
                                    index idx_second_comment (second_comment_id)
)
create table Collect_Writing(
                                id bigint primary key auto_increment comment '纯主键id',
                                user_id bigint not null comment '用户id',
                                writing_id bigint not null comment '文章的id',
                                is_collect int not null comment '收藏状态',
                                unique key uk_writing_user (user_id,writing_id),
                                index idx_user (user_id),
                                index idx_writing (writing_id)
)
create table Collect_Question(
                                 id bigint primary key auto_increment comment '纯主键id',
                                 user_id bigint not null comment '用户id',
                                 question_id bigint not null comment '文章的id',
                                 is_collect int not null comment '收藏状态',
                                 unique key uk_question_user (user_id,question_id),
                                 index idx_user (user_id),
                                 index idx_question (question_id)
)



















